package hs.kr.entrydsm.dsmhackerbe.domain.problem.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_choice")
class Choice(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val isCorrect: Boolean,

    @Column(nullable = false)
    val orderIndex: Int
)
