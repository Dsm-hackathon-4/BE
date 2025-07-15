package hs.kr.entrydsm.dsmhackerbe.domain.admin.service

import hs.kr.entrydsm.dsmhackerbe.domain.admin.entity.Admin
import hs.kr.entrydsm.dsmhackerbe.domain.admin.repository.AdminRepository
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.admin.AdminNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminService(
    private val adminRepository: AdminRepository
) {

    fun getAdminByAdminId(adminId: String): Admin {
        return adminRepository.findByAdminId(adminId) ?: throw AdminNotFoundException()
    }
}
