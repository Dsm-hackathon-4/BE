package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ReviewProblem
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReviewProblemRepository : JpaRepository<ReviewProblem, Long> {
    fun findByUserAndProblem(user: User, problem: Problem): ReviewProblem?
    
    fun findByUserAndIsCompleted(user: User, isCompleted: Boolean, pageable: Pageable): Page<ReviewProblem>
    fun findByUserAndIsCompleted(user: User, isCompleted: Boolean): List<ReviewProblem>
    
    @Query("SELECT rp FROM ReviewProblem rp JOIN rp.problem p JOIN p.category c WHERE rp.user = :user AND rp.isCompleted = :isCompleted AND c.name = :categoryName ORDER BY rp.addedAt DESC")
    fun findByUserAndIsCompletedAndCategoryName(user: User, isCompleted: Boolean, categoryName: String, pageable: Pageable): Page<ReviewProblem>
    
    fun countByUserAndIsCompleted(user: User, isCompleted: Boolean): Long
}
