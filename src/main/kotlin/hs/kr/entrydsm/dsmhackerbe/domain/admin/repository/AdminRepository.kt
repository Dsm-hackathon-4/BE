package hs.kr.entrydsm.dsmhackerbe.domain.admin.repository

import hs.kr.entrydsm.dsmhackerbe.domain.admin.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdminRepository : JpaRepository<Admin, UUID> {
    fun findByAdminId(adminId: String): Admin?
}
