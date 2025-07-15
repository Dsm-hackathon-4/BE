package hs.kr.entrydsm.dsmhackerbe.domain.problem.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_review_problem")
class ReviewProblem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @Column(nullable = false)
    val wrongAnswer: String,

    @Column(nullable = false)
    val addedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var reviewCount: Int = 0,

    @Column(nullable = false)
    var isCompleted: Boolean = false,

    var completedAt: LocalDateTime? = null
) {
    fun markAsCompleted() {
        this.isCompleted = true
        this.completedAt = LocalDateTime.now()
    }
    
    fun incrementReviewCount() {
        this.reviewCount++
    }
}
