package hs.kr.entrydsm.dsmhackerbe.domain.problem.entity

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Roadmap
import jakarta.persistence.*

@Entity
@Table(name = "tbl_problem")
class Problem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    val roadmap: Roadmap? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    val chapter: hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Chapter? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: ProblemType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val difficulty: Difficulty,

    @Column(nullable = false)
    val xpReward: Int,

    @Column(nullable = true)
    val explanation: String? = null,

    @Column
    val imageUrl: String? = null,

    @Column
    val hint: String? = null
)

enum class ProblemType {
    // 객관식형
    MULTIPLE_CHOICE,      // 기본 4지선다
    INITIAL_CHOICE,       // 초성 객관식
    BLANK_CHOICE,         // 빈칸 객관식
    WORD_CHOICE,          // 단어 객관식
    OX_CHOICE,            // OX 객관식
    IMAGE_CHOICE,         // 이미지 보고 객관식 맞추기
    
    // 주관식형
    SUBJECTIVE,           // 기본 주관식
    INITIAL_SUBJECTIVE,   // 초성 주관식
    BLANK_SUBJECTIVE,     // 빈칸 주관식
    WORD_SUBJECTIVE,      // 단어 주관식
    IMAGE_SUBJECTIVE      // 이미지 보고 주관식 맞추기
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}
