package hs.kr.entrydsm.dsmhackerbe.domain.problem.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ChoiceResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.CategoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ChoiceRepository
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.ProblemRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProblemService(
    private val problemRepository: ProblemRepository,
    private val categoryRepository: CategoryRepository,
    private val choiceRepository: ChoiceRepository
) {
    
    fun getProblems(
        categoryName: String?,
        difficulty: Difficulty?,
        type: ProblemType?,
        pageable: Pageable
    ): Page<ProblemResponse> {
        val problems = when {
            categoryName != null && difficulty != null && type != null -> {
                val category = categoryRepository.findByName(categoryName)
                    ?: throw IllegalArgumentException("카테고리를 찾을 수 없습니다: $categoryName")
                problemRepository.findByCategoryAndDifficultyAndType(category, difficulty, type, pageable)
            }
            categoryName != null && difficulty != null -> {
                val category = categoryRepository.findByName(categoryName)
                    ?: throw IllegalArgumentException("카테고리를 찾을 수 없습니다: $categoryName")
                problemRepository.findByCategoryAndDifficulty(category, difficulty, pageable)
            }
            categoryName != null -> {
                val category = categoryRepository.findByName(categoryName)
                    ?: throw IllegalArgumentException("카테고리를 찾을 수 없습니다: $categoryName")
                problemRepository.findByCategory(category, pageable)
            }
            type != null -> {
                problemRepository.findByType(type, pageable)
            }
            else -> problemRepository.findAll(pageable)
        }
        
        return problems.map { problem ->
            val choices = if (isChoiceBasedProblem(problem.type)) {
                choiceRepository.findByProblemOrderByOrderIndex(problem)
                    .map { ChoiceResponse.from(it) }
            } else null
            
            ProblemResponse.from(problem, choices)
        }
    }
    
    fun getProblemById(problemId: Long): ProblemResponse {
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw IllegalArgumentException("문제를 찾을 수 없습니다")
        
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
