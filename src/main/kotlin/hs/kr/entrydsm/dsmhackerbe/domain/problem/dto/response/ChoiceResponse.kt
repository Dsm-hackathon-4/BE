package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Choice

data class ChoiceResponse(
    val id: Long,
    val content: String,
    val orderIndex: Int
) {
    companion object {
        fun from(choice: Choice) = ChoiceResponse(
            id = choice.id!!,
            content = choice.content,
            orderIndex = choice.orderIndex
        )
    }
}
