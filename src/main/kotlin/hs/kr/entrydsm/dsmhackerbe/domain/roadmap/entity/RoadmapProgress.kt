package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tbl_roadmap_progress")
class RoadmapProgress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    val roadmap: Roadmap,

    @Column(nullable = false)
    var completedProblems: Int = 0,

    @Column(nullable = false)
    val totalProblems: Int = 6, // 고정값: Easy 2개, Medium 2개, Hard 2개

    @Column(nullable = false)
    var isCompleted: Boolean = false,

    @Column
    var completedAt: LocalDateTime? = null,

    @Column(nullable = false)
    val startedAt: LocalDateTime = LocalDateTime.now()
) {
    fun addProgress() {
        completedProblems++
        if (completedProblems >= totalProblems && !isCompleted) {
            isCompleted = true
            completedAt = LocalDateTime.now()
        }
    }

    fun getProgressRate(): Int {
        return if (totalProblems > 0) {
            ((completedProblems.toDouble() / totalProblems) * 100).toInt().coerceAtMost(100)
        } else 0
    }
}
