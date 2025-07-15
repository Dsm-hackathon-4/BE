package hs.kr.entrydsm.dsmhackerbe.domain.admin.entity

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.Role
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tbl_admin")
data class Admin(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val adminId: String,

    @Column(nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role
)
