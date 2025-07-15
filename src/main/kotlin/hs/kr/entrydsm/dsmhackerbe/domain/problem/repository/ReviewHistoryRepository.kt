package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ReviewHistory
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ReviewProblem
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewHistoryRepository : JpaRepository<ReviewHistory, Long> {
    fun findByUserAndReviewProblemOrderByReviewedAtDesc(user: User, reviewProblem: ReviewProblem): List<ReviewHistory>
    fun countByUserAndIsCorrect(user: User, isCorrect: Boolean): Long
}
