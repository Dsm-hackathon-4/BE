package hs.kr.entrydsm.dsmhackerbe.domain.integration.entity

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tbl_user_integration")
class UserIntegration(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val integrationType: IntegrationType,

    @Column(nullable = false)
    var isConnected: Boolean = false,

    @Column
    var accessToken: String? = null,

    @Column
    var refreshToken: String? = null,

    @Column
    var externalUserId: String? = null,

    @Column(nullable = false)
    val connectedAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var lastSyncAt: LocalDateTime? = null
)

enum class IntegrationType {
    GITHUB,
    NOTION
}
