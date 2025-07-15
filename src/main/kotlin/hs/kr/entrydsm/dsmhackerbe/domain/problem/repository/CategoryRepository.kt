package hs.kr.entrydsm.dsmhackerbe.domain.problem.repository

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Category?
}
