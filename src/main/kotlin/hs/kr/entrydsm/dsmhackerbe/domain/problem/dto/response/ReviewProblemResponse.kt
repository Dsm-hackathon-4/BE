package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import java.time.LocalDateTime

data class ReviewProblemResponse(
    val reviewId: Long,
    val problemId: Long,
    val title: String,
    val content: String,
    val type: ProblemType,
    val difficulty: Difficulty,
    val categoryName: String,
    val wrongAnswer: String,
    val reviewCount: Int,
    val addedAt: LocalDateTime,
    val imageUrl: String? = null,
    val hint: String? = null,
    val choices: List<ChoiceResponse>?
)

data class ReviewSolveResponse(
    val isCorrect: Boolean,
    val isReviewCompleted: Boolean,
    val explanation: String?,
    val reviewCount: Int
)
