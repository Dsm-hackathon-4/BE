package hs.kr.entrydsm.dsmhackerbe.domain.problem.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_review_history")
class ReviewHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_problem_id", nullable = false)
    val reviewProblem: ReviewProblem,

    @Column(nullable = false)
    val userAnswer: String,

    @Column(nullable = false)
    val isCorrect: Boolean,

    @Column(nullable = false)
    val reviewedAt: LocalDateTime = LocalDateTime.now()
)
