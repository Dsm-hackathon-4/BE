package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Choice
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChoiceRepository : JpaRepository<Choice, Long> {
    fun findByProblemOrderByOrderIndex(problem: Problem): List<Choice>
    fun findByProblemAndIsCorrect(problem: Problem, isCorrect: Boolean): Choice?
    
    @Query("SELECT c FROM Choice c WHERE c.problem.id = :problemId AND c.isCorrect = :isCorrect")
    fun findByProblemIdAndIsCorrect(problemId: Long, isCorrect: Boolean): Choice?
}
