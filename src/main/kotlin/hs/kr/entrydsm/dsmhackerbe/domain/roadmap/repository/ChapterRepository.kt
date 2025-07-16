package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Chapter
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Roadmap
import org.springframework.data.jpa.repository.JpaRepository

interface ChapterRepository : JpaRepository<Chapter, Long> {
    fun findByRoadmapAndIsActiveTrueOrderByOrderIndex(roadmap: Roadmap): List<Chapter>
    fun findByIsActiveTrueOrderByRoadmapIdAscOrderIndexAsc(): List<Chapter>
}
