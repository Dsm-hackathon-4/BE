package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Category
import jakarta.persistence.*

@Entity
@Table(name = "tbl_roadmap")
class Roadmap(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,

    @Column(nullable = false)
    val orderIndex: Int,

    @Column(nullable = false)
    val isActive: Boolean = true,

    @Column
    val iconUrl: String? = null,

    @Column
    val bgColor: String? = null
)
