package hs.kr.entrydsm.dsmhackerbe.domain.integration.service

import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.AiProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.SolveAiProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiProblemHistory
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.integration.repository.AiGeneratedProblemRepository
import hs.kr.entrydsm.dsmhackerbe.domain.integration.repository.AiProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.rank.service.RankingService
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AiProblemService(
    private val aiGeneratedProblemRepository: AiGeneratedProblemRepository,
    private val aiProblemHistoryRepository: AiProblemHistoryRepository,
    private val userRepository: UserRepository,
    private val rankingService: RankingService
) {
    
    fun getAiProblems(userEmail: String, pageable: Pageable): Page<AiProblemResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val problems = aiGeneratedProblemRepository.findByUserOrderByCreatedAtDesc(user, pageable)
        
        return problems.map { AiProblemResponse.from(it) }
    }
    
    fun getAiProblem(problemId: Long, userEmail: String): AiProblemResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val problem = aiGeneratedProblemRepository.findByIdOrNull(problemId)
            ?: throw IllegalArgumentException("문제를 찾을 수 없습니다")
        
        if (problem.user.id != user.id) {
            throw IllegalArgumentException("접근 권한이 없습니다")
        }
        
        return AiProblemResponse.from(problem)
    }
    
    @Transactional
    fun solveAiProblem(problemId: Long, userAnswer: String, userEmail: String): SolveAiProblemResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val problem = aiGeneratedProblemRepository.findByIdOrNull(problemId)
            ?: throw IllegalArgumentException("문제를 찾을 수 없습니다")
        
        if (problem.user.id != user.id) {
            throw IllegalArgumentException("접근 권한이 없습니다")
        }
        
        // 정답 체크
        val isCorrect = checkAnswer(problem, userAnswer)
        val xpEarned = if (isCorrect) problem.difficulty else 0
        
        // 기존 풀이 기록 확인
        val existingHistory = aiProblemHistoryRepository.findByUserAndAiProblem(user, problem)
        
        if (existingHistory == null) {
            // 첫 번째 풀이 - XP 지급
            val history = AiProblemHistory(
                user = user,
                aiProblem = problem,
                isCorrect = isCorrect,
                userAnswer = userAnswer,
                xpEarned = xpEarned
            )
            aiProblemHistoryRepository.save(history)
            
            // 사용자 XP 업데이트
            if (xpEarned > 0) {
                user.addXp(xpEarned)
                userRepository.save(user)
                rankingService.updateUserRanking(user, xpEarned)
            }
        } else {
            // 재풀이 - XP 지급 안함
            existingHistory.updateResult(isCorrect, userAnswer, 0)
            aiProblemHistoryRepository.save(existingHistory)
        }
        
        return SolveAiProblemResponse(
            isCorrect = isCorrect,
            correctAnswer = problem.correctAnswer ?: "",
            xpEarned = if (existingHistory == null && xpEarned > 0) xpEarned else 0
        )
    }
    
    fun getAiReviewSummary(userEmail: String): hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.AiReviewSummaryResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val allAiProblems = aiGeneratedProblemRepository.findByUserOrderByCreatedAtDesc(user)
        
        // 최근 24시간 이내에 생성된 문제를 "새로 생성된" 것으로 간주
        val yesterday = java.time.LocalDateTime.now().minusDays(1)
        val newProblems = allAiProblems.filter { it.createdAt.isAfter(yesterday) }
        val ongoingProblems = allAiProblems.filter { it.createdAt.isBefore(yesterday) }
        
        return hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.AiReviewSummaryResponse(
            newReviewCount = newProblems.size,
            ongoingReviewCount = ongoingProblems.size,
            totalReviewCount = allAiProblems.size
        )
    }
    
    private fun checkAnswer(problem: hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiGeneratedProblem, userAnswer: String): Boolean {
        val correctAnswer = problem.correctAnswer ?: return false
        val userAnswerNormalized = userAnswer.trim().lowercase()
        val correctAnswerNormalized = correctAnswer.trim().lowercase()
        
        return when (problem.problemType) {
            AiProblemType.CHOICE -> {
                // 객관식: 텍스트로 비교
                userAnswerNormalized == correctAnswerNormalized
            }
            
            AiProblemType.FILL_BLANK, AiProblemType.ANSWER -> {
                // 주관식: 텍스트 비교
                userAnswerNormalized == correctAnswerNormalized
            }
        }
    }
}
