package hs.kr.entrydsm.dsmhackerbe.domain.problem.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.SolveProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.UserProblemHistory
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ChoiceRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ProblemRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.SubjectiveAnswerRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.UserProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.rank.service.RankingService
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.StudyStreakService
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.UserGoalService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProblemSolveService(
    private val problemRepository: ProblemRepository,
    private val choiceRepository: ChoiceRepository,
    private val subjectiveAnswerRepository: SubjectiveAnswerRepository,
    private val userProblemHistoryRepository: UserProblemHistoryRepository,
    private val userRepository: UserRepository,
    private val reviewService: ReviewService,
    private val studyStreakService: StudyStreakService,
    private val rankingService: RankingService,
    private val userGoalService: UserGoalService
) {
    
    fun solveProblem(problemId: Long, userAnswer: String, userEmail: String): SolveProblemResponse {
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw IllegalArgumentException("문제를 찾을 수 없습니다")
        
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val isCorrect = when (problem.type) {
            // 객관식형
            ProblemType.MULTIPLE_CHOICE, 
            ProblemType.INITIAL_CHOICE,
            ProblemType.BLANK_CHOICE,
            ProblemType.WORD_CHOICE,
            ProblemType.OX_CHOICE,
            ProblemType.IMAGE_CHOICE -> checkMultipleChoice(problemId, userAnswer)
            
            // 주관식형
            ProblemType.SUBJECTIVE,
            ProblemType.INITIAL_SUBJECTIVE,
            ProblemType.BLANK_SUBJECTIVE,
            ProblemType.WORD_SUBJECTIVE,
            ProblemType.IMAGE_SUBJECTIVE -> checkSubjective(problemId, userAnswer)
        }
        
        val xpEarned = if (isCorrect) problem.xpReward else 0
        
        val existingHistory = try {
            userProblemHistoryRepository.findByUserAndProblem(user, problem)
        } catch (e: Exception) {
            // 중복 기록이 있을 경우 최신 기록 조회
            userProblemHistoryRepository.findLatestByUserAndProblem(user, problem).firstOrNull()
        }
        
        if (existingHistory == null) {
            // 첫 번째 풀이 - 새로운 기록 생성
            val history = UserProblemHistory(
                user = user,
                problem = problem,
                isCorrect = isCorrect,
                userAnswer = userAnswer,
                xpEarned = xpEarned
            )
            userProblemHistoryRepository.save(history)
        } else {
            // 이미 푼 문제 - 기존 기록 업데이트
            existingHistory.updateResult(isCorrect, userAnswer, xpEarned)
            userProblemHistoryRepository.save(existingHistory)
        }
        
        // 틀린 문제는 항상 복습 목록에 추가 (기존 기록과 무관하게)
        if (!isCorrect) {
            reviewService.addToReview(userEmail, problemId, userAnswer)
        }
        
        // 학습 활동 기록 (문제를 풀었으므로 스트릭 업데이트)
        studyStreakService.recordStudyActivity(userEmail)
        
        // 목표 진행도 업데이트 (문제를 풀 때마다)
        userGoalService.addProblemProgress(userEmail)
        
        return SolveProblemResponse(
            isCorrect = isCorrect,
            xpEarned = xpEarned,
            explanation = problem.explanation,
            xpBreakdown = null
        )
    }
    
    private fun checkMultipleChoice(problemId: Long, userAnswer: String): Boolean {
        val correctChoice = choiceRepository.findByProblemIdAndIsCorrect(problemId, true)
            ?: return false
        
        return correctChoice.id.toString() == userAnswer.trim()
    }
    
    private fun checkSubjective(problemId: Long, userAnswer: String): Boolean {
        val correctAnswer = subjectiveAnswerRepository.findByProblemId(problemId)
            ?: return false
        
        val userAnswerNormalized = userAnswer.trim().lowercase()
        val correctAnswerNormalized = correctAnswer.correctAnswer.trim().lowercase()
        
        if (userAnswerNormalized == correctAnswerNormalized) {
            return true
        }
        
        correctAnswer.keywords?.let { keywords ->
            val keywordList = keywords.split(",").map { it.trim().lowercase() }
            return keywordList.any { userAnswerNormalized.contains(it) }
        }
        
        return false
    }
}
