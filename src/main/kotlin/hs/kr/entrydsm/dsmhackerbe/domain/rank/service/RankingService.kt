package hs.kr.entrydsm.dsmhackerbe.domain.rank.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.UserProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response.RankingResponse
import hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response.UserRankInfo
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RankingService(
    private val userRepository: UserRepository,
    private val userProblemHistoryRepository: UserProblemHistoryRepository
) {
    
    fun getOverallRanking(userEmail: String, limit: Int = 50): RankingResponse {
        // 모든 사용자의 총 XP 기준으로 랭킹 생성
        val allUsers = userRepository.findAll()
        
        val userRankings = allUsers.map { user ->
            val totalXp = userProblemHistoryRepository.getTotalXpByUser(user) ?: 0
            Pair(user, totalXp)
        }
        .sortedByDescending { it.second }
        .take(limit)
        .mapIndexed { index, (user, totalXp) ->
            UserRankInfo(
                rank = index + 1,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = totalXp,
                isMe = user.email == userEmail
            )
        }
        
        val currentUser = userRepository.findByEmail(userEmail)
        val myRanking = currentUser?.let { user ->
            val myTotalXp = userProblemHistoryRepository.getTotalXpByUser(user) ?: 0
            val myRank = allUsers.count { otherUser ->
                val otherXp = userProblemHistoryRepository.getTotalXpByUser(otherUser) ?: 0
                otherXp > myTotalXp
            } + 1
            
            UserRankInfo(
                rank = myRank,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = myTotalXp,
                isMe = true
            )
        }
        
        return RankingResponse(
            rankings = userRankings,
            myRanking = myRanking,
            totalUsers = allUsers.size
        )
    }
}
