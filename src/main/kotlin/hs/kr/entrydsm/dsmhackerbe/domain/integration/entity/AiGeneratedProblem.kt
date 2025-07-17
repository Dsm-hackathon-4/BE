package hs.kr.entrydsm.dsmhackerbe.domain.integration.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_ai_generated_problem")
class AiGeneratedProblem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    val batch: GeneratedProblemBatch,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val problemType: AiProblemType,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(columnDefinition = "TEXT")
    var choices: String? = null, // JSON 형태로 저장

    @Column
    var correctAnswer: String? = null,

    @Column(nullable = false)
    val difficulty: Int = 10, // XP 값

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class AiProblemType {
    FILL_BLANK,  // 빈칸 채우기
    CHOICE,      // 객관식
    ANSWER       // 주관식
}
