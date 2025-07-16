package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.RoadmapProgress
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.ChapterProgressRepository
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.RoadmapProgressRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoadmapProgressService(
    private val roadmapProgressRepository: RoadmapProgressRepository,
    private val chapterProgressRepository: ChapterProgressRepository,
    private val userRepository: UserRepository
) {
    
    fun updateRoadmapProgress(userEmail: String, problem: Problem) {
        // 챕터에 속한 문제가 아니면 무시
        val chapter = problem.chapter ?: return
        val roadmap = chapter.roadmap
        
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
        
        // 해당 챕터가 완료되었는지 확인
        val chapterProgress = chapterProgressRepository.findByUserAndChapter(user, chapter)
        if (chapterProgress?.isCompleted == true) {
            // 챕터가 완료되었다면 로드맵 진행도 증가
            val completedChapters = chapterProgressRepository.findByUserIdAndChapterRoadmapId(user.id!!, roadmap.id!!)
                .count { it.isCompleted }
            
            // 완료된 챕터 수로 진행도 업데이트
            progress.completedProblems = completedChapters
            if (completedChapters >= 6 && !progress.isCompleted) {
                progress.isCompleted = true
                progress.completedAt = java.time.LocalDateTime.now()
            }
            roadmapProgressRepository.save(progress)
        }
    }
}
