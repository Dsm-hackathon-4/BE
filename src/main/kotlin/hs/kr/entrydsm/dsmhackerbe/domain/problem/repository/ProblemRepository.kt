package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Category
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProblemRepository : JpaRepository<Problem, Long> {
    fun findByCategoryAndDifficulty(category: Category, difficulty: Difficulty, pageable: Pageable): Page<Problem>
    fun findByCategory(category: Category, pageable: Pageable): Page<Problem>
    fun findByType(type: ProblemType, pageable: Pageable): Page<Problem>
    
    @Query("SELECT p FROM Problem p WHERE p.category = :category AND p.difficulty = :difficulty AND p.type = :type")
    fun findByCategoryAndDifficultyAndType(
        category: Category, 
        difficulty: Difficulty, 
        type: ProblemType, 
        pageable: Pageable
    ): Page<Problem>
}
