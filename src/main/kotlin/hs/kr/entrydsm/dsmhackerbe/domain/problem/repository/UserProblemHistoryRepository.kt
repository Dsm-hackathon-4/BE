package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.UserProblemHistory
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface UserProblemHistoryRepository : JpaRepository<UserProblemHistory, Long> {
    fun findByUserAndProblem(user: User, problem: Problem): UserProblemHistory?
    fun findByUserAndIsCorrect(user: User, isCorrect: Boolean, pageable: Pageable): Page<UserProblemHistory>
    
    @Query("SELECT h FROM UserProblemHistory h WHERE h.user = :user AND h.problem = :problem ORDER BY h.solvedAt DESC")
    fun findLatestByUserAndProblem(user: User, problem: Problem): List<UserProblemHistory>
    
    @Query("SELECT h FROM UserProblemHistory h JOIN h.problem p JOIN p.category c WHERE h.user = :user AND h.isCorrect = :isCorrect AND c.name = :categoryName ORDER BY h.solvedAt DESC")
    fun findByUserAndIsCorrectAndCategoryName(user: User, isCorrect: Boolean, categoryName: String, pageable: Pageable): Page<UserProblemHistory>
    
    @Query("SELECT SUM(h.xpEarned) FROM UserProblemHistory h WHERE h.user = :user")
    fun getTotalXpByUser(user: User): Int?
    
    @Query("SELECT h FROM UserProblemHistory h WHERE h.user = :user AND h.solvedAt BETWEEN :startDate AND :endDate")
    fun findByUserAndSolvedAtBetween(user: User, startDate: LocalDateTime, endDate: LocalDateTime): List<UserProblemHistory>
    
    @Query("SELECT h FROM UserProblemHistory h WHERE h.user = :user ORDER BY h.solvedAt DESC")
    fun findByUserOrderBySolvedAtDesc(user: User): List<UserProblemHistory>
    
    fun countByUser(user: User): Long
}
