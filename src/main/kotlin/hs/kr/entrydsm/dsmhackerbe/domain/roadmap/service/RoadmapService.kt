package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ChoiceResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.CategoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ChoiceRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ProblemRepository
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.ChapterResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.RoadmapDetailResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.RoadmapProblems
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.RoadmapResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.entity.RoadmapProgress
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.ChapterProgressRepository
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.ChapterRepository
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.RoadmapProgressRepository
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.repository.RoadmapRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RoadmapService(
    private val roadmapRepository: RoadmapRepository,
    private val roadmapProgressRepository: RoadmapProgressRepository,
    private val chapterRepository: ChapterRepository,
    private val chapterProgressRepository: ChapterProgressRepository,
    private val problemRepository: ProblemRepository,
    private val choiceRepository: ChoiceRepository,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository
) {
    
    fun getAllRoadmaps(userEmail: String): List<RoadmapResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val roadmaps = roadmapRepository.findByIsActiveTrueOrderByOrderIndex()
        val userProgress = roadmapProgressRepository.findByUserId(user.id!!)
            .associateBy { it.roadmap.id }
        
        return roadmaps.map { roadmap ->
            RoadmapResponse.from(roadmap, userProgress[roadmap.id])
        }
    }
    
    fun getRoadmapsByCategory(categoryId: Long, userEmail: String): List<RoadmapResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val category = categoryRepository.findById(categoryId).orElse(null)
            ?: throw IllegalArgumentException("카테고리를 찾을 수 없습니다")
        
        val roadmaps = roadmapRepository.findByCategoryAndIsActiveTrueOrderByOrderIndex(category)
        val userProgress = roadmapProgressRepository.findByUserId(user.id!!)
            .associateBy { it.roadmap.id }
        
        return roadmaps.map { roadmap ->
            RoadmapResponse.from(roadmap, userProgress[roadmap.id])
        }
    }
    
    fun getRoadmapDetail(roadmapId: Long, userEmail: String): RoadmapDetailResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val roadmap = roadmapRepository.findByIdOrNull(roadmapId)
            ?: throw IllegalArgumentException("로드맵을 찾을 수 없습니다")
        
        val progress = roadmapProgressRepository.findByUserAndRoadmap(user, roadmap)
        val roadmapResponse = RoadmapResponse.from(roadmap, progress)
        
        // 로드맵에 속한 문제들을 난이도별로 조회
        val allProblems = problemRepository.findByRoadmapOrderByDifficultyAscIdAsc(roadmap)
        
        val problemsByDifficulty = allProblems.groupBy { it.difficulty }
        
        val easyProblems = problemsByDifficulty[Difficulty.EASY]?.map { toProblemResponse(it) } ?: emptyList()
        val mediumProblems = problemsByDifficulty[Difficulty.MEDIUM]?.map { toProblemResponse(it) } ?: emptyList()
        val hardProblems = problemsByDifficulty[Difficulty.HARD]?.map { toProblemResponse(it) } ?: emptyList()
        
        val problems = RoadmapProblems(
            easy = easyProblems,
            medium = mediumProblems,
            hard = hardProblems
        )
        
        return RoadmapDetailResponse(roadmapResponse, problems)
    }
    
    fun getRoadmapChapters(roadmapId: Long, userEmail: String): List<ChapterResponse> {
        println("=== DEBUG: getRoadmapChapters ===")
        println("roadmapId: $roadmapId, userEmail: $userEmail")
        
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        println("user found: ${user.email}")
        
        val roadmap = roadmapRepository.findByIdOrNull(roadmapId)
            ?: throw IllegalArgumentException("로드맵을 찾을 수 없습니다")
        println("roadmap found: ${roadmap.title}")
        
        val chapters = chapterRepository.findByRoadmapAndIsActiveTrueOrderByOrderIndex(roadmap)
        println("chapters count: ${chapters.size}")
        chapters.forEach { chapter ->
            println("chapter: ${chapter.title}, isActive: ${chapter.isActive}")
        }
        
        val userProgress = chapterProgressRepository.findByUserIdAndChapterRoadmapId(user.id!!, roadmapId)
            .associateBy { it.chapter.id }
        println("userProgress count: ${userProgress.size}")
        
        return chapters.map { chapter ->
            ChapterResponse.from(chapter, userProgress[chapter.id])
        }
    }
    
    fun getChapterProblems(chapterId: Long, userEmail: String): List<ProblemResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val chapter = chapterRepository.findByIdOrNull(chapterId)
            ?: throw IllegalArgumentException("챕터를 찾을 수 없습니다")
        
        val problems = problemRepository.findByChapterOrderByIdAsc(chapter)
        
        return problems.map { problem ->
            toProblemResponse(problem)
        }
    }
    
    private fun toProblemResponse(problem: hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem): ProblemResponse {
        val choices = if (isChoiceBasedProblem(problem.type)) {
            choiceRepository.findByProblemOrderByOrderIndex(problem)
                .map { ChoiceResponse.from(it) }
        } else null
        
        return ProblemResponse.from(problem, choices)
    }
    
    private fun isChoiceBasedProblem(type: ProblemType): Boolean {
        return when (type) {
            ProblemType.MULTIPLE_CHOICE,
            ProblemType.INITIAL_CHOICE,
            ProblemType.BLANK_CHOICE,
            ProblemType.WORD_CHOICE,
            ProblemType.OX_CHOICE,
            ProblemType.IMAGE_CHOICE -> true
            else -> false
        }
    }
}
