package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_chapter_progress")
class ChapterProgress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    val chapter: Chapter,

    @Column(nullable = false)
    var completedProblems: Int = 0,

    @Column(nullable = false)
    val totalProblems: Int = 10, // 챕터당 문제 10개

    @Column(nullable = false)
    var isCompleted: Boolean = false,

    @Column
    var completedAt: LocalDateTime? = null,

    @Column(nullable = false)
    val startedAt: LocalDateTime = LocalDateTime.now()
) {
    fun addProgress() {
        completedProblems++
        // 10문제를 모두 풀면 챕터 완료 (정답 여부 상관없이)
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
