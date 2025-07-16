package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Category
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "카테고리 정보")
data class CategoryResponse(
    @Schema(title = "id: Long (카테고리 ID)", description = "카테고리의 고유 식별자", example = "1")
    val id: Long,
    
    @Schema(title = "name: String (카테고리 이름)", description = "카테고리명", example = "네트워크")
    val name: String,
    
    @Schema(title = "description: String (카테고리 설명)", description = "카테고리에 대한 설명", example = "네트워크 프로토콜, 통신 관련 문제")
    val description: String,
    
    @Schema(title = "iconUrl: String (아이콘 URL)", description = "카테고리 아이콘 이미지 URL", example = "https://example.com/network-icon.png", nullable = true)
    val iconUrl: String?
) {
    companion object {
        fun from(category: Category) = CategoryResponse(
            id = category.id!!,
            name = category.name,
            description = category.description,
            iconUrl = category.iconUrl
        )
    }
}
