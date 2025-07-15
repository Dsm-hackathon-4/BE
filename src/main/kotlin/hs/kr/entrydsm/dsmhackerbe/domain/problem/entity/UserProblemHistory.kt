package hs.kr.entrydsm.dsmhackerbe.domain.problem.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_user_problem_history", 
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "problem_id"])
    ]
)
class UserProblemHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @Column(nullable = false)
    var isCorrect: Boolean,

    @Column(columnDefinition = "TEXT")
    var userAnswer: String,

    @Column(nullable = false)
    var solvedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var xpEarned: Int
) {
    fun updateResult(isCorrect: Boolean, userAnswer: String, xpEarned: Int) {
        this.isCorrect = isCorrect
        this.userAnswer = userAnswer
        this.solvedAt = LocalDateTime.now()
        // XP는 처음 정답일 때만 지급
        if (isCorrect && !this.isCorrect) {
            this.xpEarned = xpEarned
        }
    }
}
