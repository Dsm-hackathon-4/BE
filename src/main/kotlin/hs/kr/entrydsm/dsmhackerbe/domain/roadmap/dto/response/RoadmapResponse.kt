package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Roadmap
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.RoadmapProgress
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로드맵 정보")
data class RoadmapResponse(
    @Schema(title = "id: Long (로드맵 ID)", description = "로드맵 고유 식별자", example = "1")
    val id: Long,
    
    @Schema(title = "title: String (로드맵 제목)", description = "로드맵 이름", example = "데이터베이스 기초")
    val title: String,
    
    @Schema(title = "description: String (로드맵 설명)", description = "로드맵에 대한 상세 설명", example = "데이터베이스의 기본 개념을 학습합니다")
    val description: String,
    
    @Schema(title = "categoryName: String (카테고리)", description = "소속 카테고리명", example = "데이터베이스")
    val categoryName: String,
    
    @Schema(title = "isCompleted: Boolean (완료 여부)", description = "6개 문제를 모두 맞췄는지 여부", example = "false")
    val isCompleted: Boolean,
    
    @Schema(title = "iconUrl: String (아이콘 URL)", description = "로드맵 아이콘 이미지", example = "https://example.com/db-icon.png", nullable = true)
    val iconUrl: String?,
    
    @Schema(title = "bgColor: String (배경색)", description = "로드맵 배경색 코드", example = "#4CAF50", nullable = true)
    val bgColor: String?
) {
    companion object {
        fun from(roadmap: Roadmap, progress: RoadmapProgress?): RoadmapResponse {
            return RoadmapResponse(
                id = roadmap.id!!,
                title = roadmap.title,
                description = roadmap.description,
                categoryName = roadmap.category.name,
                isCompleted = progress?.isCompleted ?: false,
                iconUrl = roadmap.iconUrl,
                bgColor = roadmap.bgColor
            )
        }
    }
}

