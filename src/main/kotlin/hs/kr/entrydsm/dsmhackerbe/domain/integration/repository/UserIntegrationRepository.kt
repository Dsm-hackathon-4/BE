package hs.kr.entrydsm.dsmhackerbe.domain.integration.repository

import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.IntegrationType
import hs.kr.entrydsm.dsmhackerbe.domain.integration.entity.UserIntegration
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserIntegrationRepository : JpaRepository<UserIntegration, Long> {
    fun findByUserAndIntegrationType(user: User, type: IntegrationType): UserIntegration?
    fun findByUserAndIntegrationTypeAndIsConnectedTrue(user: User, type: IntegrationType): UserIntegration?
}
