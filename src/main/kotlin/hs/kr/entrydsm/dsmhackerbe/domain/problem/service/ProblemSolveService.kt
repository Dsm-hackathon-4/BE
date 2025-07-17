package hs.kr.entrydsm.dsmhackerbe.domain.problem.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ChapterCompleteInfo
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.SolveProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.UserProblemHistory
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ChoiceRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ProblemRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.SubjectiveAnswerRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.UserProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.rank.service.RankingService
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.service.ChapterProgressService
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.service.RoadmapProgressService
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.StudyStreakService
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.UserGoalService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProblemSolveService(
    private val problemRepository: ProblemRepository,
    private val choiceRepository: ChoiceRepository,
    private val subjectiveAnswerRepository: SubjectiveAnswerRepository,
    private val userProblemHistoryRepository: UserProblemHistoryRepository,
    private val userRepository: UserRepository,
    private val reviewService: ReviewService,
    private val studyStreakService: StudyStreakService,
    private val rankingService: RankingService,
    private val userGoalService: UserGoalService,
    private val roadmapProgressService: RoadmapProgressService,
    private val chapterProgressService: ChapterProgressService
) {
    
    fun solveProblem(problemId: Long, userAnswer: String, userEmail: String): SolveProblemResponse {
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw IllegalArgumentException("문제를 찾을 수 없습니다")
        
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val isCorrect = when (problem.type) {
            // 객관식형
            ProblemType.MULTIPLE_CHOICE, 
            ProblemType.INITIAL_CHOICE,
            ProblemType.WORD_CHOICE,
            ProblemType.OX_CHOICE,
            ProblemType.IMAGE_CHOICE -> checkMultipleChoice(problemId, userAnswer)
            
            // 주관식형 (빈칸채우기 포함)
            ProblemType.BLANK_CHOICE,
            ProblemType.SUBJECTIVE,
            ProblemType.INITIAL_SUBJECTIVE,
            ProblemType.BLANK_SUBJECTIVE,
            ProblemType.WORD_SUBJECTIVE,
            ProblemType.IMAGE_SUBJECTIVE -> checkSubjective(problemId, userAnswer)
        }
        
        val xpEarned = if (isCorrect) problem.xpReward else 0
        
        val existingHistory = try {
            userProblemHistoryRepository.findByUserAndProblem(user, problem)
        } catch (e: Exception) {
            // 중복 기록이 있을 경우 최신 기록 조회
            userProblemHistoryRepository.findLatestByUserAndProblem(user, problem).firstOrNull()
        }
        
        if (existingHistory == null) {
            // 첫 번째 풀이 - 새로운 기록 생성
            val history = UserProblemHistory(
                user = user,
                problem = problem,
                isCorrect = isCorrect,
                userAnswer = userAnswer,
                xpEarned = xpEarned
            )
            userProblemHistoryRepository.save(history)
            
            // 사용자 XP 업데이트 (첫 번째 풀이만)
            if (xpEarned > 0) {
                user.addXp(xpEarned)
                userRepository.save(user)
                
                // 랭킹 업데이트
                rankingService.updateUserRanking(user, xpEarned)
            }
        } else {
            // 이미 푼 문제 - 기존 기록 업데이트 (XP는 첫 번째만 지급)
            existingHistory.updateResult(isCorrect, userAnswer, 0) // XP는 0으로
            userProblemHistoryRepository.save(existingHistory)
        }
        
        // 틀린 문제는 항상 복습 목록에 추가 (기존 기록과 무관하게)
        if (!isCorrect) {
            reviewService.addToReview(userEmail, problemId, userAnswer)
        }
        
        // 학습 활동 기록 (문제를 풀었으므로 스트릭 업데이트)
        studyStreakService.recordStudyActivity(userEmail)
        
        // 목표 진행도 업데이트 (문제를 풀 때마다)
        userGoalService.addProblemProgress(userEmail)
        
        // 챕터 진행도 업데이트 (정답 여부 상관없이)
        chapterProgressService.updateChapterProgress(userEmail, problem)
        
        // 챕터 완료 여부 확인 (진행도 업데이트 후에 체크)
        val chapterCompleteInfo = if (problem.chapter != null) {
            checkChapterCompletion(user, problem.chapter!!)
        } else null
        
        // 로드맵 진행도 업데이트 (챕터 완료 시)
        roadmapProgressService.updateRoadmapProgress(userEmail, problem)
        
        // 정답 조회
        val correctAnswer = getCorrectAnswer(problem)
        
        // 실제 지급된 XP (재풀이는 0)
        val actualXpEarned = if (existingHistory == null && xpEarned > 0) xpEarned else 0
        
        return SolveProblemResponse(
            isCorrect = isCorrect,
            correctAnswer = correctAnswer,
            xpEarned = actualXpEarned,
            explanation = problem.explanation,
            xpBreakdown = null,
            chapterComplete = chapterCompleteInfo
        )
    }
    
    private fun checkChapterCompletion(user: hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User, chapter: hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.Chapter): ChapterCompleteInfo? {
        val chapterProgress = chapterProgressService.getChapterProgress(user, chapter)
            ?: return null
        
        // 정확히 10문제를 완료했을 때
        if (chapterProgress.completedProblems == 10) {
            // 해당 챕터의 모든 풀이 기록 조회
            val chapterHistory = userProblemHistoryRepository.findByUserOrderBySolvedAtDesc(user)
                .filter { it.problem.chapter?.id == chapter.id }
                .take(10) // 최근 10개만
            
            // 챕터 완료 통계 계산
            val totalXp = chapterHistory.sumOf { it.xpEarned }
            val correctCount = chapterHistory.count { it.isCorrect }
            val totalCount = chapterHistory.size
            val accuracyRate = if (totalCount > 0) (correctCount * 100) / totalCount else 0
            
            // 진행도 초기화
            chapterProgress.resetProgress()
            chapterProgressService.saveChapterProgress(chapterProgress)
            
            return ChapterCompleteInfo(
                isChapterCompleted = true,
                chapterTitle = chapter.title,
                totalXp = totalXp,
                correctCount = correctCount,
                totalCount = totalCount,
                accuracyRate = accuracyRate
            )
        }
        
        return null
    }
    
    private fun checkMultipleChoice(problemId: Long, userAnswer: String): Boolean {
        val correctChoice = choiceRepository.findByProblemIdAndIsCorrect(problemId, true)
            ?: return false
        
        return correctChoice.id.toString() == userAnswer.trim()
    }
    
    private fun checkSubjective(problemId: Long, userAnswer: String): Boolean {
        val correctAnswer = subjectiveAnswerRepository.findByProblemId(problemId)
            ?: return false
        
        val userAnswerNormalized = userAnswer.trim().lowercase()
        val correctAnswerNormalized = correctAnswer.correctAnswer.trim().lowercase()
        
        if (userAnswerNormalized == correctAnswerNormalized) {
            return true
        }
        
        correctAnswer.keywords?.let { keywords ->
            val keywordList = keywords.split(",").map { it.trim().lowercase() }
            return keywordList.any { userAnswerNormalized.contains(it) }
        }
        
        return false
    }
    
    private fun getCorrectAnswer(problem: hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem): String? {
        // 먼저 객관식 테이블에서 확인
        val choiceAnswer = choiceRepository.findByProblemIdAndIsCorrect(problem.id!!, true)?.content
        if (choiceAnswer != null) {
            return choiceAnswer
        }
        
        // 객관식에 없으면 주관식 테이블에서 확인
        val subjectiveAnswer = subjectiveAnswerRepository.findByProblemId(problem.id!!)?.correctAnswer
        return subjectiveAnswer
    }
}
