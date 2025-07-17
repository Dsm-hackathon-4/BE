package hs.kr.entrydsm.dsmhackerbe.domain.integration.repository

import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.GeneratedProblemBatch
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface GeneratedProblemBatchRepository : JpaRepository<GeneratedProblemBatch, Long> {
    fun findByUserOrderByRequestedAtDesc(user: User): List<GeneratedProblemBatch>
}
