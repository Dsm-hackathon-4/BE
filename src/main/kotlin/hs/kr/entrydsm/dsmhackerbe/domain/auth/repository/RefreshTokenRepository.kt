package hs.kr.entrydsm.dsmhackerbe.domain.auth.repository

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    fun findByToken(token: String): RefreshToken?
}
