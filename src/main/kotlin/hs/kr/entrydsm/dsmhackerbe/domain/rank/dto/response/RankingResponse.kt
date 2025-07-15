package hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response

data class RankingResponse(
    val rankings: List<UserRankInfo>,
    val myRanking: UserRankInfo?,
    val totalUsers: Int
)

data class UserRankInfo(
    val rank: Int,
    val userId: String,
    val name: String,
    val profileImageUrl: String,
    val totalXp: Int,
    val isMe: Boolean = false
)
