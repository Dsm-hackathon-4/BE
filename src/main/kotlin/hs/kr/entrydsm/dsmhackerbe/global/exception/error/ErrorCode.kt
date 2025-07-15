package hs.kr.entrydsm.dsmhackerbe.global.exception.error

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val statusCode: Int,
    val message: String
) {

    // OAuth
    INVALID_KAKAO_OAUTH_RESPONSE(400, "OAuth 응답이 유효하지 않습니다."),
    UNSUPPORTED_OAUTH_PROVIDER(400, "지원하지 않는 OAuth2 제공자입니다"),
    INVALID_KAKAO_ACCESS_TOKEN(401, "유효하지 않은 kakao access 토큰입니다."),
    OAUTH_AUTHENTICATION_FAILED(401, "OAuth2 인증에 실패했습니다"),

    // Security
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "토큰이 만료 되었습니다."),
    PASSWORD_MISS_MATCH(401, "비밀번호가 일치하지 않습니다"),

    // User
    USER_NOT_FOUND(404, "일치하는 유저를 찾을 수 없습니다"),
    USER_ALREADY_EXIST(409, "유저가 이미 존재합니다"),

    // Admin
    ADMIN_NOT_FOUND(404, "일치하는 어드민을 찾을 수 없습니다"),

    // Request
    BAD_REQUEST(400, "잘못된 요청입니다"),

    // Server
    INTERNAL_SERVER_ERROR(500, "서버 오류 발생");
}
