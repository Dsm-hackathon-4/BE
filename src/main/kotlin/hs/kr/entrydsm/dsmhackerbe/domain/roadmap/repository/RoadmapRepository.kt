package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Category
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Roadmap
import org.springframework.data.jpa.repository.JpaRepository

interface RoadmapRepository : JpaRepository<Roadmap, Long> {
    fun findByCategoryAndIsActiveTrueOrderByOrderIndex(category: Category): List<Roadmap>
    fun findByIsActiveTrueOrderByOrderIndex(): List<Roadmap>
}
