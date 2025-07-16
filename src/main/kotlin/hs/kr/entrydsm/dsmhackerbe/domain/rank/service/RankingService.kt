package hs.kr.entrydsm.dsmhackerbe.domain.rank.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.UserProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response.RankingResponse
import hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response.UserRankInfo
import hs.kr.entrydsm.dsmhackerbe.domain.rank.repository.UserRankingRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

@Service
@Transactional(readOnly = true)
class RankingService(
    private val userRepository: UserRepository,
    private val userProblemHistoryRepository: UserProblemHistoryRepository,
    private val userRankingRepository: UserRankingRepository
) {
    
    @Transactional
    fun updateUserRanking(user: hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User, xpEarned: Int) {
        val today = LocalDate.now()
        val weekStart = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
        val monthStart = today.withDayOfMonth(1)
        
        var userRanking = userRankingRepository.findByUserIdAndRankingDate(user.id!!, today)
        
        if (userRanking == null) {
            // 오늘 첫 문제 풀이 - 새 기록 생성
            userRanking = hs.kr.entrydsm.dsmhackerbe.domain.rank.entity.UserRanking(
                userId = user.id!!,
                rankingDate = today,
                weekStartDate = weekStart,
                monthStartDate = monthStart,
                dailyXp = xpEarned,
                weeklyXp = xpEarned,
                monthlyXp = xpEarned
            )
        } else {
            // 기존 기록에 XP 추가
            userRanking.addXp(xpEarned)
        }
        
        userRankingRepository.save(userRanking)
    }
    
    fun getOverallRanking(userEmail: String, limit: Int = 50): RankingResponse {
        // User 엔티티의 totalXp 기준으로 랭킹 생성
        val allUsers = userRepository.findAll()
        
        val userRankings = allUsers
            .sortedByDescending { it.totalXp }
            .take(limit)
            .mapIndexed { index, user ->
                UserRankInfo(
                    rank = index + 1,
                    userId = user.id.toString(),
                    name = user.name,
                    profileImageUrl = user.profileImageUrl,
                    totalXp = user.totalXp,
                    isMe = user.email == userEmail
                )
            }
        
        val currentUser = userRepository.findByEmail(userEmail)
        val myRanking = currentUser?.let { user ->
            val myRank = allUsers.count { otherUser ->
                otherUser.totalXp > user.totalXp
            } + 1
            
            UserRankInfo(
                rank = myRank,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = user.totalXp,
                isMe = true
            )
        }
        
        return RankingResponse(
            rankings = userRankings,
            myRanking = myRanking,
            totalUsers = allUsers.size
        )
    }

    fun getDailyRanking(userEmail: String, limit: Int = 50): RankingResponse {
        val today = LocalDate.now()
        val allUsers = userRepository.findAll()
        
        val userRankings = allUsers.map { user ->
            val dailyXp = userRankingRepository.findByUserIdAndRankingDate(user.id!!, today)?.dailyXp ?: 0
            Pair(user, dailyXp)
        }
        .sortedByDescending { it.second }
        .take(limit)
        .mapIndexed { index, (user, dailyXp) ->
            UserRankInfo(
                rank = index + 1,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = dailyXp,
                isMe = user.email == userEmail
            )
        }
        
        val currentUser = userRepository.findByEmail(userEmail)
        val myRanking = currentUser?.let { user ->
            val myDailyXp = userRankingRepository.findByUserIdAndRankingDate(user.id!!, today)?.dailyXp ?: 0
            val myRank = allUsers.count { otherUser ->
                val otherDailyXp = userRankingRepository.findByUserIdAndRankingDate(otherUser.id!!, today)?.dailyXp ?: 0
                otherDailyXp > myDailyXp
            } + 1
            
            UserRankInfo(
                rank = myRank,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = myDailyXp,
                isMe = true
            )
        }
        
        return RankingResponse(
            rankings = userRankings,
            myRanking = myRanking,
            totalUsers = allUsers.size
        )
    }

    fun getWeeklyRanking(userEmail: String, limit: Int = 50): RankingResponse {
        val today = LocalDate.now()
        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val allUsers = userRepository.findAll()
        
        val userRankings = allUsers.map { user ->
            val weeklyXp = userRankingRepository.getUserWeeklyXp(user.id!!, weekStart) ?: 0
            Pair(user, weeklyXp)
        }
        .sortedByDescending { it.second }
        .take(limit)
        .mapIndexed { index, (user, weeklyXp) ->
            UserRankInfo(
                rank = index + 1,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = weeklyXp,
                isMe = user.email == userEmail
            )
        }
        
        val currentUser = userRepository.findByEmail(userEmail)
        val myRanking = currentUser?.let { user ->
            val myWeeklyXp = userRankingRepository.getUserWeeklyXp(user.id!!, weekStart) ?: 0
            val myRank = allUsers.count { otherUser ->
                val otherWeeklyXp = userRankingRepository.getUserWeeklyXp(otherUser.id!!, weekStart) ?: 0
                otherWeeklyXp > myWeeklyXp
            } + 1
            
            UserRankInfo(
                rank = myRank,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = myWeeklyXp,
                isMe = true
            )
        }
        
        return RankingResponse(
            rankings = userRankings,
            myRanking = myRanking,
            totalUsers = allUsers.size
        )
    }

    fun getMonthlyRanking(userEmail: String, limit: Int = 50): RankingResponse {
        val today = LocalDate.now()
        val monthStart = today.withDayOfMonth(1)
        val allUsers = userRepository.findAll()
        
        val userRankings = allUsers.map { user ->
            val monthlyXp = userRankingRepository.getUserMonthlyXp(user.id!!, monthStart) ?: 0
            Pair(user, monthlyXp)
        }
        .sortedByDescending { it.second }
        .take(limit)
        .mapIndexed { index, (user, monthlyXp) ->
            UserRankInfo(
                rank = index + 1,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = monthlyXp,
                isMe = user.email == userEmail
            )
        }
        
        val currentUser = userRepository.findByEmail(userEmail)
        val myRanking = currentUser?.let { user ->
            val myMonthlyXp = userRankingRepository.getUserMonthlyXp(user.id!!, monthStart) ?: 0
            val myRank = allUsers.count { otherUser ->
                val otherMonthlyXp = userRankingRepository.getUserMonthlyXp(otherUser.id!!, monthStart) ?: 0
                otherMonthlyXp > myMonthlyXp
            } + 1
            
            UserRankInfo(
                rank = myRank,
                userId = user.id.toString(),
                name = user.name,
                profileImageUrl = user.profileImageUrl,
                totalXp = myMonthlyXp,
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
