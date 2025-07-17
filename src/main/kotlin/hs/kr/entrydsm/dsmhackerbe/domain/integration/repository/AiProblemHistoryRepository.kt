package hs.kr.entrydsm.dsmhackerbe.domain.integration.repository

import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiGeneratedProblem
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.AiProblemHistory
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface AiProblemHistoryRepository : JpaRepository<AiProblemHistory, Long> {
    fun findByUserAndAiProblem(user: User, aiProblem: AiGeneratedProblem): AiProblemHistory?
    fun findByUserOrderBySolvedAtDesc(user: User): List<AiProblemHistory>
}
