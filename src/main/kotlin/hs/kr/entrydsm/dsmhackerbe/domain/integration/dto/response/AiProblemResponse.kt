package hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiGeneratedProblem
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiProblemType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "AI 생성 문제 정보")
data class AiProblemResponse(
    @Schema(description = "문제 ID", example = "1")
    val id: Long,
    
    @Schema(description = "문제 타입", example = "FILL_BLANK")
    val problemType: AiProblemType,
    
    @Schema(description = "문제 내용", example = "스택은 {{LIFO}} 방식으로 동작한다.")
    val content: String,
    
    @Schema(description = "선택지 목록 (객관식인 경우)", example = "[\"LIFO\", \"FIFO\", \"RANDOM\", \"SORT\"]")
    val choices: List<String>? = null,
    
    @Schema(description = "난이도 (XP)", example = "10")
    val difficulty: Int,
    
    @Schema(description = "생성일시", example = "2025-07-17T08:30:00")
    val createdAt: String
) {
    companion object {
        fun from(problem: AiGeneratedProblem): AiProblemResponse {
            val choicesList = problem.choices?.let { 
                com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().readValue(it, List::class.java) as List<String>
            }
            
            return AiProblemResponse(
                id = problem.id!!,
                problemType = problem.problemType,
                content = problem.content,
                choices = choicesList,
                difficulty = problem.difficulty,
                createdAt = problem.createdAt.toString()
            )
        }
    }
}
