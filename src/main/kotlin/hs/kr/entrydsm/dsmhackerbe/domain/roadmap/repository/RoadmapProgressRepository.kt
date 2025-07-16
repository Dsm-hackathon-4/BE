package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Roadmap
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.RoadmapProgress
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoadmapProgressRepository : JpaRepository<RoadmapProgress, Long> {
    fun findByUserAndRoadmap(user: User, roadmap: Roadmap): RoadmapProgress?
    fun findByUserId(userId: UUID): List<RoadmapProgress>
    fun findByUserIdAndRoadmapId(userId: UUID, roadmapId: Long): RoadmapProgress?
}
