package hs.kr.entrydsm.dsmhackerbe.domain.integration.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_generated_problem_batch")
class GeneratedProblemBatch(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val sourceType: IntegrationType,

    @Column(nullable = false)
    var problemCount: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: BatchStatus = BatchStatus.PENDING,

    @Column
    var errorMessage: String? = null,

    @Column(nullable = false)
    val requestedAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var completedAt: LocalDateTime? = null
)

enum class BatchStatus {
    PENDING,    // 요청됨
    PROCESSING, // 처리 중
    COMPLETED,  // 완료
    FAILED      // 실패
}
