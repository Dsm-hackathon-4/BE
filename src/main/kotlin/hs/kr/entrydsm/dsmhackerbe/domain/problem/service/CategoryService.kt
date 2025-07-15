package hs.kr.entrydsm.dsmhackerbe.domain.problem.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.CategoryResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    
    fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAll()
            .map { CategoryResponse.from(it) }
    }
}
