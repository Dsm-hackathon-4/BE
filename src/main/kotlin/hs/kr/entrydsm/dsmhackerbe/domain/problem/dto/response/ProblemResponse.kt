package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType

data class ProblemResponse(
    val id: Long,
    val title: String,
    val content: String,
    val type: ProblemType,
    val difficulty: Difficulty,
    val xpReward: Int,
    val category: CategoryResponse,
    val imageUrl: String? = null,
    val hint: String? = null,
    val choices: List<ChoiceResponse>? = null
) {
    companion object {
        fun from(problem: Problem, choices: List<ChoiceResponse>? = null) = ProblemResponse(
            id = problem.id!!,
            title = problem.title,
            content = problem.content,
            type = problem.type,
            difficulty = problem.difficulty,
            xpReward = problem.xpReward,
            category = CategoryResponse.from(problem.category),
            imageUrl = problem.imageUrl,
            hint = problem.hint,
            choices = choices
        )
    }
}
