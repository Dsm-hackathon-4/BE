package hs.kr.entrydsm.dsmhackerbe.domain.problem.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_category")
class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = true)
    val iconUrl: String? = null,
    
    @Column(nullable = true)
    val color: String? = null
)
