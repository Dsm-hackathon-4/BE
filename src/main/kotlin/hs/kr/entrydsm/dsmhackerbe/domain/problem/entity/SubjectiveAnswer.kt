package hs.kr.entrydsm.dsmhackerbe.domain.problem.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tbl_subjective_answer")
class SubjectiveAnswer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @Column(nullable = false, columnDefinition = "TEXT")
    val correctAnswer: String,

    @Column(columnDefinition = "TEXT")
    val keywords: String? = null
)
