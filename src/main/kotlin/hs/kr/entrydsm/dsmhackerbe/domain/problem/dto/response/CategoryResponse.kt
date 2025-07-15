package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Category

data class CategoryResponse(
    val id: Long,
    val name: String,
    val description: String,
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
