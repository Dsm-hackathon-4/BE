package hs.kr.entrydsm.dsmhackerbe.domain.rank.repository

import hs.kr.entrydsm.dsmhackerbe.domain.rank.entity.UserRanking
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.util.*

interface UserRankingRepository : JpaRepository<UserRanking, Long> {
    
    fun findByUserIdAndRankingDate(userId: UUID, rankingDate: LocalDate): UserRanking?
    
    // 일간 랭킹
    @Query("""
        SELECT ur FROM UserRanking ur 
        WHERE ur.rankingDate = :date 
        ORDER BY ur.dailyXp DESC, ur.userId ASC
    """)
    fun findDailyRanking(date: LocalDate, pageable: Pageable): List<UserRanking>
    
    @Query("""
        SELECT COUNT(ur) FROM UserRanking ur 
        WHERE ur.rankingDate = :date AND ur.dailyXp > 0
    """)
    fun countDailyActiveUsers(date: LocalDate): Long
    
    @Query("""
        SELECT COUNT(ur) FROM UserRanking ur 
        WHERE ur.rankingDate = :date AND ur.dailyXp > :userXp
    """)
    fun countUsersAboveInDaily(date: LocalDate, userXp: Int): Long
    
    // 주간 랭킹
    @Query("""
        SELECT ur FROM UserRanking ur 
        WHERE ur.weekStartDate = :weekStart 
        GROUP BY ur.userId
        ORDER BY SUM(ur.weeklyXp) DESC, ur.userId ASC
    """)
    fun findWeeklyRanking(weekStart: LocalDate, pageable: Pageable): List<UserRanking>
    
    @Query("""
        SELECT COUNT(DISTINCT ur.userId) FROM UserRanking ur 
        WHERE ur.weekStartDate = :weekStart AND ur.weeklyXp > 0
    """)
    fun countWeeklyActiveUsers(weekStart: LocalDate): Long
    
    @Query("""
        SELECT SUM(ur.weeklyXp) FROM UserRanking ur 
        WHERE ur.userId = :userId AND ur.weekStartDate = :weekStart
    """)
    fun getUserWeeklyXp(userId: UUID, weekStart: LocalDate): Int?
    
    // 월간 랭킹
    @Query("""
        SELECT ur FROM UserRanking ur 
        WHERE ur.monthStartDate = :monthStart 
        GROUP BY ur.userId
        ORDER BY SUM(ur.monthlyXp) DESC, ur.userId ASC
    """)
    fun findMonthlyRanking(monthStart: LocalDate, pageable: Pageable): List<UserRanking>
    
    @Query("""
        SELECT COUNT(DISTINCT ur.userId) FROM UserRanking ur 
        WHERE ur.monthStartDate = :monthStart AND ur.monthlyXp > 0
    """)
    fun countMonthlyActiveUsers(monthStart: LocalDate): Long
    
    @Query("""
        SELECT SUM(ur.monthlyXp) FROM UserRanking ur 
        WHERE ur.userId = :userId AND ur.monthStartDate = :monthStart
    """)
    fun getUserMonthlyXp(userId: UUID, monthStart: LocalDate): Int?
}
