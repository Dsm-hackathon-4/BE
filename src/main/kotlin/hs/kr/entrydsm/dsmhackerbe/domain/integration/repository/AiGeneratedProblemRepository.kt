package hs.kr.entrydsm.dsmhackerbe.domain.integration.repository

import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiGeneratedProblem
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AiGeneratedProblemRepository : JpaRepository<AiGeneratedProblem, Long> {
    fun findByUserOrderByCreatedAtDesc(user: User): List<AiGeneratedProblem>
    fun findByUserOrderByCreatedAtDesc(user: User, pageable: Pageable): Page<AiGeneratedProblem>
}
