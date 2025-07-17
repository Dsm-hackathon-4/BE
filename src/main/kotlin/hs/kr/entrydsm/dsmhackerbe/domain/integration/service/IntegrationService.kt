package hs.kr.entrydsm.dsmhackerbe.domain.integration.service

import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.BatchStatus
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.GeneratedProblemBatch
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.IntegrationType
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.UserIntegration
import hs.kr.entrydsm.dsmhackerbe.domain.integration.repository.GeneratedProblemBatchRepository
import hs.kr.entrydsm.dsmhackerbe.domain.integration.repository.UserIntegrationRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class IntegrationService(
    private val userRepository: UserRepository,
    private val userIntegrationRepository: UserIntegrationRepository,
    private val generatedProblemBatchRepository: GeneratedProblemBatchRepository,
    private val aiProblemGeneratorService: AiProblemGeneratorService
) {
    
    fun generateProblemsFromIntegration(userEmail: String): String {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        // 배치 작업 생성
        val batch = GeneratedProblemBatch(
            user = user,
            sourceType = IntegrationType.GITHUB, // 임시로 GITHUB 설정
            status = BatchStatus.PENDING
        )
        val savedBatch = generatedProblemBatchRepository.save(batch)
        
        // 비동기로 AI 서버에 문제 생성 요청
        aiProblemGeneratorService.generateProblemsAsync(savedBatch.id!!, user.id.toString())
        
        return "문제 생성 요청이 완료되었습니다"
    }
    
    fun getBatchStatus(userEmail: String): Map<String, Any?> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val latestBatch = generatedProblemBatchRepository.findByUserOrderByRequestedAtDesc(user).firstOrNull()
        
        return if (latestBatch != null) {
            mapOf(
                "status" to latestBatch.status.name,
                "problemCount" to latestBatch.problemCount,
                "requestedAt" to latestBatch.requestedAt.toString(),
                "completedAt" to latestBatch.completedAt?.toString(),
                "errorMessage" to latestBatch.errorMessage
            )
        } else {
            mapOf("status" to "NO_BATCH")
        }
    }
}
