package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Chapter
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.ChapterProgress
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChapterProgressRepository : JpaRepository<ChapterProgress, Long> {
    fun findByUserAndChapter(user: User, chapter: Chapter): ChapterProgress?
    fun findByUserId(userId: UUID): List<ChapterProgress>
    fun findByUserIdAndChapterRoadmapId(userId: UUID, roadmapId: Long): List<ChapterProgress>
}
