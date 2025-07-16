package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Choice
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "객관식 선택지 정보")
data class ChoiceResponse(
    @Schema(title = "id: Long (선택지 ID)", description = "선택지의 고유 식별자", example = "1")
    val id: Long,
    
    @Schema(title = "content: String (선택지 내용)", description = "선택지 텍스트", example = "TCP는 연결지향, UDP는 비연결지향")
    val content: String,
    
    @Schema(title = "orderIndex: Int (선택지 순서)", description = "선택지 표시 순서", example = "1")
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
