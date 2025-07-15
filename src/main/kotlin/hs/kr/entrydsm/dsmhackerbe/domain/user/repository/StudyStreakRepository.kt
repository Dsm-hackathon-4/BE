package hs.kr.entrydsm.dsmhackerbe.domain.user.repository

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.StudyStreak
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StudyStreakRepository : JpaRepository<StudyStreak, Long> {
    fun findByUserId(userId: UUID): StudyStreak?
}
