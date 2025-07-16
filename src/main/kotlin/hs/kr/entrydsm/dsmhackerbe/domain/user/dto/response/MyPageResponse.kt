package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "마이페이지 정보")
data class MyPageResponse(
    @Schema(title = "userInfo: UserInfoResponse (사용자 정보)", description = "사용자 기본 정보")
    val userInfo: UserInfoResponse,
    
    @Schema(title = "statistics: UserStatisticsResponse (사용자 통계)", description = "사용자 학습 통계")
    val statistics: UserStatisticsResponse,
    
    @Schema(title = "xpInfo: XpInfoResponse (XP 정보)", description = "사용자 경험치 정보")
    val xpInfo: XpInfoResponse,
    
    @Schema(title = "studyStreak: StudyStreakInfoResponse (학습 스트릭)", description = "연속 학습 정보")
    val studyStreak: StudyStreakInfoResponse,
    
    @Schema(title = "studyStatus: StudyStatusResponse (학습 현황)", description = "전반적인 학습 현황")
    val studyStatus: StudyStatusResponse,
    
    @Schema(title = "goals: UserGoalsResponse (목표 정보)", description = "사용자 학습 목표")
    val goals: UserGoalsResponse
)

@Schema(description = "사용자 기본 정보")
data class UserInfoResponse(
    @Schema(title = "id: String (사용자 ID)", description = "사용자 고유 식별자", example = "550e8400-e29b-41d4-a716-446655440000")
    val id: String,
    
    @Schema(title = "name: String (이름)", description = "사용자 이름", example = "홍길동")
    val name: String,
    
    @Schema(title = "email: String (이메일)", description = "사용자 이메일", example = "user@example.com")
    val email: String,
    
    @Schema(title = "profileImageUrl: String (프로필 이미지)", description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    val profileImageUrl: String,
    
    @Schema(title = "gender: Gender (성별)", description = "사용자 성별 (MALE, FEMALE, UNKNOWN)", example = "MALE")
    val gender: Gender,
    
    @Schema(title = "age: String (나이)", description = "사용자 나이", example = "25")
    val age: String,
    
    @Schema(title = "provider: String (로그인 제공자)", description = "로그인 방식 (GOOGLE, KAKAO, LOCAL)", example = "GOOGLE")
    val provider: String
)

@Schema(description = "사용자 학습 통계")
data class UserStatisticsResponse(
    @Schema(title = "totalXp: Int (총 XP)", description = "누적 획득 경험치", example = "1250")
    val totalXp: Int,
    
    @Schema(title = "currentStreak: Int (현재 스트릭)", description = "연속 학습 일수", example = "7")
    val currentStreak: Int,
    
    @Schema(title = "dailyRank: Int (일일 순위)", description = "오늘의 랭킹", example = "3")
    val dailyRank: Int,
    
    @Schema(title = "weeklyRank: Int (주간 순위)", description = "이번 주 랭킹", example = "5")
    val weeklyRank: Int,
    
    @Schema(title = "monthlyRank: Int (월간 순위)", description = "이번 달 랭킹", example = "12")
    val monthlyRank: Int,
    
    @Schema(title = "totalProblems: Int (총 풀이 문제)", description = "총 풀어본 문제 수", example = "45")
    val totalProblems: Int,
    
    @Schema(title = "correctProblems: Int (맞춘 문제)", description = "정답 처리된 문제 수", example = "38")
    val correctProblems: Int,
    
    @Schema(title = "accuracy: Int (정확도)", description = "정답률 (퍼센트)", example = "84")
    val accuracy: Int
)

@Schema(description = "XP 정보")
data class XpInfoResponse(
    @Schema(title = "totalXp: Int (총 XP)", description = "누적 총 경험치", example = "1250")
    val totalXp: Int,
    
    @Schema(title = "todayXp: Int (오늘 XP)", description = "오늘 획득한 경험치", example = "85")
    val todayXp: Int,
    
    @Schema(title = "weeklyXp: Int (주간 XP)", description = "이번 주 획득한 경험치", example = "320")
    val weeklyXp: Int,
    
    @Schema(title = "monthlyXp: Int (월간 XP)", description = "이번 달 획득한 경험치", example = "950")
    val monthlyXp: Int
)

@Schema(description = "연속 학습 정보")
data class StudyStreakInfoResponse(
    @Schema(title = "currentStreak: Int (현재 스트릭)", description = "현재 연속 학습 일수", example = "7")
    val currentStreak: Int,
    
    @Schema(title = "maxStreak: Int (최대 스트릭)", description = "최고 연속 학습 일수", example = "15")
    val maxStreak: Int,
    
    @Schema(title = "totalStudyDays: Int (총 학습일)", description = "누적 학습한 날의 수", example = "23")
    val totalStudyDays: Int,
    
    @Schema(title = "lastStudyDate: LocalDate (마지막 학습일)", description = "가장 최근 학습한 날짜", example = "2025-07-15", nullable = true)
    val lastStudyDate: LocalDate?
)

@Schema(description = "학습 현황")
data class StudyStatusResponse(
    @Schema(title = "totalProblems: Int (총 문제 수)", description = "풀어본 총 문제 수", example = "45")
    val totalProblems: Int,
    
    @Schema(title = "correctProblems: Int (맞춘 문제 수)", description = "정답 처리된 문제 수", example = "38")
    val correctProblems: Int,
    
    @Schema(title = "reviewProblems: Int (복습 문제 수)", description = "복습 목록에 있는 문제 수", example = "7")
    val reviewProblems: Int,
    
    @Schema(title = "recentActivity: List<RecentActivityResponse> (최근 활동)", description = "최근 7일간의 학습 활동")
    val recentActivity: List<RecentActivityResponse>
)

@Schema(description = "일별 학습 활동")
data class RecentActivityResponse(
    @Schema(title = "date: LocalDate (날짜)", description = "학습한 날짜", example = "2025-07-15")
    val date: LocalDate,
    
    @Schema(title = "problemCount: Int (문제 수)", description = "해당 날짜에 푼 문제 수", example = "5")
    val problemCount: Int,
    
    @Schema(title = "xpEarned: Int (획득 XP)", description = "해당 날짜에 획득한 경험치", example = "75")
    val xpEarned: Int
)

@Schema(description = "사용자 목표 정보")
data class UserGoalsResponse(
    @Schema(title = "dailyGoal: Int (일일 목표)", description = "하루 문제 풀이 목표", example = "10")
    val dailyGoal: Int,
    
    @Schema(title = "todayProgress: Int (오늘 진행도)", description = "오늘 푼 문제 수", example = "7")
    val todayProgress: Int,
    
    @Schema(title = "achievementRate: Int (달성률)", description = "목표 달성률 (퍼센트)", example = "70")
    val achievementRate: Int,
    
    @Schema(title = "streak: Int (목표 달성 스트릭)", description = "연속 목표 달성 일수", example = "3")
    val streak: Int
)
