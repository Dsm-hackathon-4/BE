package hs.kr.entrydsm.dsmhackerbe.domain.user.entity

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.Role
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tbl_user")
class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, columnDefinition = "CHAR(70)")
    val password: String,

    @Column(nullable = false, columnDefinition = "CHAR(30)")
    val name: String,

    @Column(nullable = false)
    val profileImageUrl: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val gender: Gender,

    val age: String,

    // 연속 학습 일수 있어야 함

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @Column(nullable = false)
    val coin: Int,

    @Column(nullable = false)
    val provider: String
)

enum class Gender {
    UNKNOWN,
    MALE,
    FEMALE
}
