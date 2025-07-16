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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @Column(nullable = false)
    val coin: Int,

    @Column(nullable = false)
    var totalXp: Int = 0,

    @Column(nullable = false)
    val provider: String
) {
    fun addXp(xp: Int) {
        totalXp += xp
    }
}

enum class Gender {
    UNKNOWN,
    MALE,
    FEMALE
}
