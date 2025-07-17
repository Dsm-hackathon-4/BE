package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.ChapterProgress
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.ChapterProgressRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChapterProgressService(
    private val chapterProgressRepository: ChapterProgressRepository,
    private val userRepository: UserRepository
) {
    
    fun updateChapterProgress(userEmail: String, problem: Problem) {
        // 챕터에 속한 문제가 아니면 무시
        val chapter = problem.chapter ?: return
        
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        var progress = chapterProgressRepository.findByUserAndChapter(user, chapter)
        
        if (progress == null) {
            // 첫 번째 문제 풀이 시 진행도 생성
            progress = ChapterProgress(
                user = user,
                chapter = chapter
            )
        }
        
        // 매번 문제 풀 때마다 카운트 증가
        progress.addProgress()
        chapterProgressRepository.save(progress)
    }
    
    fun getChapterProgress(user: hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User, chapter: hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Chapter): ChapterProgress? {
        return chapterProgressRepository.findByUserAndChapter(user, chapter)
    }
    
    fun saveChapterProgress(chapterProgress: ChapterProgress) {
        chapterProgressRepository.save(chapterProgress)
    }
}
