package hs.kr.entrydsm.dsmhackerbe.domain.problem.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ChoiceResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ReviewProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ReviewSolveResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ReviewSummaryResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ReviewHistory
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ReviewProblem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.*
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.StudyStreakService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewService(
    private val reviewProblemRepository: ReviewProblemRepository,
    private val reviewHistoryRepository: ReviewHistoryRepository,
    private val userRepository: UserRepository,
    private val problemRepository: ProblemRepository,
    private val choiceRepository: ChoiceRepository,
    private val subjectiveAnswerRepository: SubjectiveAnswerRepository,
    private val studyStreakService: StudyStreakService
) {
    
    fun addToReview(userEmail: String, problemId: Long, wrongAnswer: String) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw IllegalArgumentException("문제를 찾을 수 없습니다")
        
        val existingReview = reviewProblemRepository.findByUserAndProblem(user, problem)
        
        if (existingReview == null) {
            val reviewProblem = ReviewProblem(
                user = user,
                problem = problem,
                wrongAnswer = wrongAnswer
            )
            reviewProblemRepository.save(reviewProblem)
        }
    }
    
    @Transactional(readOnly = true)
    fun getReviewProblems(userEmail: String, pageable: Pageable): Page<ReviewProblemResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val reviewProblems = reviewProblemRepository.findByUserAndIsCompleted(user, false, pageable)
        
        val responses = reviewProblems.content.map { reviewProblem ->
            val problem = reviewProblem.problem
            val choices = if (isChoiceBasedProblem(problem.type)) {
                choiceRepository.findByProblemOrderByOrderIndex(problem)
                    .map { choice ->
                        ChoiceResponse(
                            id = choice.id!!,
                            content = choice.content,
                            orderIndex = choice.orderIndex
                        )
                    }
            } else null
            
            ReviewProblemResponse(
                reviewId = reviewProblem.id!!,
                problemId = problem.id!!,
                title = problem.title,
                content = problem.content,
                type = problem.type,
                difficulty = problem.difficulty,
                categoryName = problem.category.name,
                wrongAnswer = reviewProblem.wrongAnswer,
                reviewCount = reviewProblem.reviewCount,
                addedAt = reviewProblem.addedAt,
                imageUrl = problem.imageUrl,
                hint = problem.hint,
                choices = choices
            )
        }
        
        return PageImpl(responses, pageable, reviewProblems.totalElements)
    }
    
    @Transactional(readOnly = true)
    fun getReviewProblemsByCategory(userEmail: String, categoryName: String, pageable: Pageable): Page<ReviewProblemResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val reviewProblems = reviewProblemRepository.findByUserAndIsCompletedAndCategoryName(user, false, categoryName, pageable)
        
        val responses = reviewProblems.content.map { reviewProblem ->
            val problem = reviewProblem.problem
            val choices = if (isChoiceBasedProblem(problem.type)) {
                choiceRepository.findByProblemOrderByOrderIndex(problem)
                    .map { choice ->
                        ChoiceResponse(
                            id = choice.id!!,
                            content = choice.content,
                            orderIndex = choice.orderIndex
                        )
                    }
            } else null
            
            ReviewProblemResponse(
                reviewId = reviewProblem.id!!,
                problemId = problem.id!!,
                title = problem.title,
                content = problem.content,
                type = problem.type,
                difficulty = problem.difficulty,
                categoryName = problem.category.name,
                wrongAnswer = reviewProblem.wrongAnswer,
                reviewCount = reviewProblem.reviewCount,
                addedAt = reviewProblem.addedAt,
                imageUrl = problem.imageUrl,
                hint = problem.hint,
                choices = choices
            )
        }
        
        return PageImpl(responses, pageable, reviewProblems.totalElements)
    }
    
    fun solveReviewProblem(userEmail: String, reviewId: Long, userAnswer: String): ReviewSolveResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val reviewProblem = reviewProblemRepository.findByIdOrNull(reviewId)
            ?: throw IllegalArgumentException("복습 문제를 찾을 수 없습니다")
        
        if (reviewProblem.user.id != user.id) {
            throw IllegalArgumentException("권한이 없습니다")
        }
        
        if (reviewProblem.isCompleted) {
            throw IllegalArgumentException("이미 완료된 복습 문제입니다")
        }
        
        val problem = reviewProblem.problem
        val isCorrect = when (problem.type) {
            // 객관식형
            ProblemType.MULTIPLE_CHOICE,
            ProblemType.INITIAL_CHOICE,
            ProblemType.BLANK_CHOICE,
            ProblemType.WORD_CHOICE,
            ProblemType.OX_CHOICE,
            ProblemType.IMAGE_CHOICE -> checkMultipleChoice(problem.id!!, userAnswer)
            
            // 주관식형
            ProblemType.SUBJECTIVE,
            ProblemType.INITIAL_SUBJECTIVE,
            ProblemType.BLANK_SUBJECTIVE,
            ProblemType.WORD_SUBJECTIVE,
            ProblemType.IMAGE_SUBJECTIVE -> checkSubjective(problem.id!!, userAnswer)
        }
        
        reviewProblem.incrementReviewCount()
        
        if (isCorrect) {
            reviewProblem.markAsCompleted()
        }
        
        val reviewHistory = ReviewHistory(
            user = user,
            problem = problem,
            reviewProblem = reviewProblem,
            userAnswer = userAnswer,
            isCorrect = isCorrect
        )
        
        reviewHistoryRepository.save(reviewHistory)
        reviewProblemRepository.save(reviewProblem)
        
        // 복습 활동도 학습 활동으로 기록
        studyStreakService.recordStudyActivity(userEmail)
        
        return ReviewSolveResponse(
            isCorrect = isCorrect,
            isReviewCompleted = reviewProblem.isCompleted,
            explanation = problem.explanation,
            reviewCount = reviewProblem.reviewCount
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
    
    @Transactional(readOnly = true)
    fun getReviewSummary(userEmail: String): ReviewSummaryResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val allReviews = reviewProblemRepository.findByUserAndIsCompleted(user, false)
        
        // 최근 24시간 이내에 추가된 복습을 "새로 생성된" 것으로 간주
        val yesterday = java.time.LocalDateTime.now().minusDays(1)
        val newReviews = allReviews.filter { it.addedAt.isAfter(yesterday) }
        val ongoingReviews = allReviews.filter { it.addedAt.isBefore(yesterday) }
        
        return ReviewSummaryResponse(
            newReviewCount = newReviews.size,
            ongoingReviewCount = ongoingReviews.size,
            totalReviewCount = allReviews.size
        )
    }
    
    private fun isChoiceBasedProblem(type: ProblemType): Boolean {
        return when (type) {
            ProblemType.MULTIPLE_CHOICE,
            ProblemType.INITIAL_CHOICE,
            ProblemType.BLANK_CHOICE,
            ProblemType.WORD_CHOICE,
            ProblemType.OX_CHOICE,
            ProblemType.IMAGE_CHOICE -> true
            else -> false
        }
    }
}
