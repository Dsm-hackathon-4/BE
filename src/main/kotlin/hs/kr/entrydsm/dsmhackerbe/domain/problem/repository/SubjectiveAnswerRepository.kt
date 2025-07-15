package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.SubjectiveAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SubjectiveAnswerRepository : JpaRepository<SubjectiveAnswer, Long> {
    fun findByProblem(problem: Problem): SubjectiveAnswer?
    
    @Query("SELECT sa FROM SubjectiveAnswer sa WHERE sa.problem.id = :problemId")
    fun findByProblemId(problemId: Long): SubjectiveAnswer?
}
