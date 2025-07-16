package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_chapter")
class Chapter(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    val roadmap: Roadmap,

    @Column(nullable = false)
    val orderIndex: Int,

    @Column(nullable = false)
    val isActive: Boolean = true,

    @Column
    val iconUrl: String? = null
)
