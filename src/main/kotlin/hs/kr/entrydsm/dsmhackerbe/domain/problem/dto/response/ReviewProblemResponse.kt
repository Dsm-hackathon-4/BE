package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "복습 문제 정보")
data class ReviewProblemResponse(
    @Schema(title = "reviewId: Long (복습 기록 ID)", description = "복습 기록의 고유 식별자", example = "1")
    val reviewId: Long,
    
    @Schema(title = "problemId: Long (문제 ID)", description = "문제의 고유 식별자", example = "5")
    val problemId: Long,
    
    @Schema(title = "title: String (문제 제목)", description = "문제의 제목", example = "TCP와 UDP의 차이점")
    val title: String,
    
    @Schema(title = "content: String (문제 내용)", description = "문제의 본문 내용", example = "TCP와 UDP 프로토콜의 가장 큰 차이점은 무엇인가요?")
    val content: String,
    
    @Schema(
        title = "type: ProblemType (문제 유형)",
        description = "MULTIPLE_CHOICE(4지선다), INITIAL_CHOICE(초성), BLANK_CHOICE(빈칸), WORD_CHOICE(단어), OX_CHOICE(O/X), IMAGE_CHOICE(이미지), SUBJECTIVE(주관식), INITIAL_SUBJECTIVE(초성주관식), BLANK_SUBJECTIVE(빈칸주관식), WORD_SUBJECTIVE(단어주관식), IMAGE_SUBJECTIVE(이미지주관식)",
        example = "MULTIPLE_CHOICE"
    )
    val type: ProblemType,
    
    @Schema(
        title = "difficulty: Difficulty (난이도)",
        description = "EASY(쉬움), MEDIUM(보통), HARD(어려움)",
        example = "EASY"
    )
    val difficulty: Difficulty,
    
    @Schema(title = "categoryName: String (카테고리 이름)", description = "문제가 속한 카테고리", example = "네트워크")
    val categoryName: String,
    
    @Schema(title = "wrongAnswer: String (틀린 답안)", description = "사용자가 제출한 틀린 답안", example = "TCP는 비연결지향, UDP는 연결지향")
    val wrongAnswer: String,
    
    @Schema(title = "reviewCount: Int (복습 횟수)", description = "이 문제를 복습한 횟수", example = "2")
    val reviewCount: Int,
    
    @Schema(title = "addedAt: LocalDateTime (추가 시간)", description = "복습 목록에 추가된 시간", example = "2025-07-15T10:45:42.222Z")
    val addedAt: LocalDateTime,
    
    @Schema(title = "imageUrl: String (이미지 URL)", description = "문제에 포함된 이미지 URL", example = "https://example.com/problem-image.jpg", nullable = true)
    val imageUrl: String? = null,
    
    @Schema(title = "hint: String (힌트)", description = "문제 해결을 위한 힌트", example = "프로토콜의 연결 방식을 생각해보세요", nullable = true)
    val hint: String? = null,
    
    @Schema(title = "choices: List<ChoiceResponse> (선택지 목록)", description = "객관식 문제의 선택지들", nullable = true)
    val choices: List<ChoiceResponse>?
)

@Schema(description = "복습 문제 풀이 결과")
data class ReviewSolveResponse(
    @Schema(description = "정답 여부", example = "true")
    val isCorrect: Boolean,
    
    @Schema(description = "복습 완료 여부", example = "false")
    val isReviewCompleted: Boolean,
    
    @Schema(description = "문제 해설", example = "TCP는 연결지향적이고 신뢰성을 보장합니다.", nullable = true)
    val explanation: String?,
    
    @Schema(description = "복습 횟수", example = "3")
    val reviewCount: Int
)
