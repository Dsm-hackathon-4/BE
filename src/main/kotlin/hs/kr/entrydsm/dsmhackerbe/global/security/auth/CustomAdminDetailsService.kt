package hs.kr.entrydsm.dsmhackerbe.global.security.auth

import hs.kr.entrydsm.dsmhackerbe.domain.admin.service.AdminService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class CustomAdminDetailsService(
    private val adminService: AdminService
) : UserDetailsService {

    override fun loadUserByUsername(adminId: String): UserDetails {
        val admin = adminService.getAdminByAdminId(adminId)
        return AuthDetails(admin.adminId, admin.role.name, AuthDetails.EMPTY_ATTRIBUTES)
    }
}
