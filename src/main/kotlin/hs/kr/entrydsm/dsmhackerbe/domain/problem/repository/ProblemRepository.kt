package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Category
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Roadmap
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProblemRepository : JpaRepository<Problem, Long> {
    fun findByDifficulty(difficulty: Difficulty, pageable: Pageable): Page<Problem>
    fun findByCategoryAndDifficulty(category: Category, difficulty: Difficulty, pageable: Pageable): Page<Problem>
    fun findByCategory(category: Category, pageable: Pageable): Page<Problem>
    fun findByType(type: ProblemType, pageable: Pageable): Page<Problem>
    
    // 로드맵 관련 메서드
    fun findByRoadmapOrderByDifficultyAscIdAsc(roadmap: Roadmap): List<Problem>
    fun findByRoadmapAndDifficulty(roadmap: Roadmap, difficulty: Difficulty): List<Problem>
    
    @Query("SELECT p FROM Problem p WHERE p.category = :category AND p.difficulty = :difficulty AND p.type = :type")
    fun findByCategoryAndDifficultyAndType(
        category: Category, 
        difficulty: Difficulty, 
        type: ProblemType, 
        pageable: Pageable
    ): Page<Problem>
}
