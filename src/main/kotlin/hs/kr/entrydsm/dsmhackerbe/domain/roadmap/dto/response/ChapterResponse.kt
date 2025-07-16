package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Chapter
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.ChapterProgress
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "챕터 정보")
data class ChapterResponse(
    @Schema(title = "id: Long (챕터 ID)", description = "챕터 고유 식별자", example = "1")
    val id: Long,
    
    @Schema(title = "title: String (챕터 제목)", description = "챕터 이름", example = "SQL 기초")
    val title: String,
    
    @Schema(title = "description: String (챕터 설명)", description = "챕터에 대한 상세 설명", example = "SQL의 기본 문법을 학습합니다")
    val description: String,
    
    @Schema(title = "orderIndex: Int (순서)", description = "챕터 순서", example = "1")
    val orderIndex: Int,
    
    @Schema(title = "completedProblems: Int (완료 문제 수)", description = "푼 문제 개수", example = "7")
    val completedProblems: Int,
    
    @Schema(title = "totalProblems: Int (전체 문제 수)", description = "챕터 내 전체 문제 개수", example = "10")
    val totalProblems: Int,
    
    @Schema(title = "progressRate: Int (진행률)", description = "진행률 퍼센트", example = "70")
    val progressRate: Int,
    
    @Schema(title = "isCompleted: Boolean (완료 여부)", description = "10개 문제를 모두 맞췄는지 여부", example = "false")
    val isCompleted: Boolean,
    
    @Schema(title = "iconUrl: String (아이콘 URL)", description = "챕터 아이콘 이미지", example = "https://example.com/sql-icon.png", nullable = true)
    val iconUrl: String?
) {
    companion object {
        fun from(chapter: Chapter, progress: ChapterProgress?): ChapterResponse {
            return ChapterResponse(
                id = chapter.id!!,
                title = chapter.title,
                description = chapter.description,
                orderIndex = chapter.orderIndex,
                completedProblems = progress?.completedProblems ?: 0,
                totalProblems = progress?.totalProblems ?: 10,
                progressRate = progress?.getProgressRate() ?: 0,
                isCompleted = progress?.isCompleted ?: false,
                iconUrl = chapter.iconUrl
            )
        }
    }
}
