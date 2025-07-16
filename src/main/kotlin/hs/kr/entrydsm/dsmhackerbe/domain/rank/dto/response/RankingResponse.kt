package hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "랭킹 조회 응답")
data class RankingResponse(
    @Schema(title = "rankings: List<UserRankInfo> (랭킹 목록)", description = "상위 랭커들의 목록", example = "[{...}]")
    val rankings: List<UserRankInfo>,
    
    @Schema(title = "myRanking: UserRankInfo (내 랭킹)", description = "현재 사용자의 랭킹 정보", nullable = true)
    val myRanking: UserRankInfo?,
    
    @Schema(title = "totalUsers: Int (전체 사용자 수)", description = "랭킹에 참여한 총 사용자 수", example = "150")
    val totalUsers: Int
)

@Schema(description = "사용자 랭킹 정보")
data class UserRankInfo(
    @Schema(title = "rank: Int (순위)", description = "사용자의 순위", example = "1")
    val rank: Int,
    
    @Schema(title = "userId: String (사용자 ID)", description = "사용자 고유 식별자", example = "550e8400-e29b-41d4-a716-446655440000")
    val userId: String,
    
    @Schema(title = "name: String (사용자 이름)", description = "사용자의 닉네임", example = "홍길동")
    val name: String,
    
    @Schema(title = "profileImageUrl: String (프로필 이미지)", description = "사용자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
    val profileImageUrl: String,
    
    @Schema(title = "totalXp: Int (총 XP)", description = "사용자가 획득한 총 경험치", example = "1250")
    val totalXp: Int,
    
    @Schema(title = "isMe: Boolean (본인 여부)", description = "현재 로그인한 사용자인지 여부", example = "true")
    val isMe: Boolean = false
)
