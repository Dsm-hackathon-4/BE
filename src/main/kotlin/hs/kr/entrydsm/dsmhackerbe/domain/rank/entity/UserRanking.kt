package hs.kr.entrydsm.dsmhackerbe.domain.rank.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "tbl_user_ranking", 
    indexes = [
        Index(name = "idx_user_date", columnList = "user_id, ranking_date"),
        Index(name = "idx_date_xp", columnList = "ranking_date, daily_xp DESC"),
        Index(name = "idx_week_xp", columnList = "week_start_date, weekly_xp DESC"),
        Index(name = "idx_month_xp", columnList = "month_start_date, monthly_xp DESC")
    ]
)
class UserRanking(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val rankingDate: LocalDate,

    @Column(nullable = false)
    val weekStartDate: LocalDate,

    @Column(nullable = false)
    val monthStartDate: LocalDate,

    @Column(nullable = false)
    var dailyXp: Int = 0,

    @Column(nullable = false)
    var weeklyXp: Int = 0,

    @Column(nullable = false)
    var monthlyXp: Int = 0
) {
    fun addXp(xp: Int) {
        this.dailyXp += xp
        this.weeklyXp += xp
        this.monthlyXp += xp
    }
}
