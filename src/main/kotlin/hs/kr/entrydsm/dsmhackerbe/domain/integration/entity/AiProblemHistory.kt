package hs.kr.entrydsm.dsmhackerbe.domain.integration.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_ai_problem_history")
class AiProblemHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_problem_id", nullable = false)
    val aiProblem: AiGeneratedProblem,

    @Column(nullable = false)
    var isCorrect: Boolean,

    @Column(nullable = false)
    var userAnswer: String,

    @Column(nullable = false)
    var xpEarned: Int,

    @Column(nullable = false)
    val solvedAt: LocalDateTime = LocalDateTime.now()
) {
    fun updateResult(isCorrect: Boolean, userAnswer: String, xpEarned: Int) {
        this.isCorrect = isCorrect
        this.userAnswer = userAnswer
        this.xpEarned = xpEarned
    }
}
