package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.RoadmapProgress
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.RoadmapProgressRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoadmapProgressService(
    private val roadmapProgressRepository: RoadmapProgressRepository,
    private val userRepository: UserRepository
) {
    
    fun updateRoadmapProgress(userEmail: String, problem: Problem) {
        // 로드맵에 속한 문제가 아니면 무시
        val roadmap = problem.roadmap ?: return
        
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        var progress = roadmapProgressRepository.findByUserAndRoadmap(user, roadmap)
        
        if (progress == null) {
            // 첫 번째 문제 풀이 시 진행도 생성
            progress = RoadmapProgress(
                user = user,
                roadmap = roadmap
            )
            roadmapProgressRepository.save(progress)
        }
        
        // 이미 완료된 로드맵이면 더 이상 진행도 업데이트 안함
        if (progress.isCompleted) {
            return
        }
        
        progress.addProgress()
        roadmapProgressRepository.save(progress)
    }
}
