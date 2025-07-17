package hs.kr.entrydsm.dsmhackerbe.domain.integration.service

import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.AiProblemsResponse
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.BatchStatus
import hs.kr.entrydsm.dsmhackerbe.domain.integration.repository.AiGeneratedProblemRepository
import hs.kr.entrydsm.dsmhackerbe.domain.integration.repository.GeneratedProblemBatchRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
@Transactional
class AiProblemGeneratorService(
    private val restTemplate: RestTemplate,
    private val generatedProblemBatchRepository: GeneratedProblemBatchRepository,
    private val aiGeneratedProblemRepository: AiGeneratedProblemRepository
) {
    
    private val aiServerBaseUrl = "https://integrations-223.loca.lt"
    
    @Async
    fun generateProblemsAsync(batchId: Long, userId: String) {
        try {
            val batch = generatedProblemBatchRepository.findById(batchId).orElseThrow()
            batch.status = BatchStatus.PROCESSING
            generatedProblemBatchRepository.save(batch)
            
            // AI 서버에 문제 생성 요청
            val aiProblems = requestProblemsFromAi(userId)
            
            // DB에 문제 저장
            val savedProblems = saveProblemsToDatabase(aiProblems, batch.user, batch)
            
            // 배치 완료 처리
            batch.status = BatchStatus.COMPLETED
            batch.problemCount = savedProblems.size
            batch.completedAt = LocalDateTime.now()
            generatedProblemBatchRepository.save(batch)
            
        } catch (e: Exception) {
            // 배치 실패 처리
            val batch = generatedProblemBatchRepository.findById(batchId).orElseThrow()
            batch.status = BatchStatus.FAILED
            batch.errorMessage = e.message
            generatedProblemBatchRepository.save(batch)
        }
    }
    
    private fun requestProblemsFromAi(userId: String): AiProblemsResponse {
        val url = "$aiServerBaseUrl/summary/$userId"
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        
        val requestEntity = HttpEntity<Any>(headers)
        
        val response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            AiProblemsResponse::class.java
        )
        
        return response.body ?: throw RuntimeException("AI 서버로부터 응답을 받을 수 없습니다")
    }
    
    private fun saveProblemsToDatabase(
        aiProblems: AiProblemsResponse, 
        user: hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User,
        batch: hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.GeneratedProblemBatch
    ): List<hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiGeneratedProblem> {
        val savedProblems = mutableListOf<hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiGeneratedProblem>()
        
        for (aiProblem in aiProblems.problems) {
            val problemType = when (aiProblem.type) {
                "fill_blank" -> hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiProblemType.FILL_BLANK
                "choice" -> hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiProblemType.CHOICE
                "answer" -> hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiProblemType.ANSWER
                else -> continue
            }
            
            val problem = hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiGeneratedProblem(
                user = user,
                batch = batch,
                problemType = problemType,
                content = aiProblem.text,
                choices = aiProblem.choices?.let { com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().writeValueAsString(it) },
                correctAnswer = aiProblem.correctAnswer ?: extractAnswerFromBlank(aiProblem.text),
                difficulty = 10
            )
            
            val savedProblem = aiGeneratedProblemRepository.save(problem)
            savedProblems.add(savedProblem)
        }
        
        return savedProblems
    }
    
    private fun extractAnswerFromBlank(text: String): String {
        val regex = "\\{\\{(.+?)\\}\\}".toRegex()
        val matchResult = regex.find(text)
        return matchResult?.groupValues?.get(1) ?: ""
    }
}
