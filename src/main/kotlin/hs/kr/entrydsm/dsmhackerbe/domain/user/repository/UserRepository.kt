package hs.kr.entrydsm.dsmhackerbe.domain.user.repository

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
