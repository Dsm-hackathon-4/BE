package hs.kr.entrydsm.dsmhackerbe.global.exception.domain.user

import hs.kr.entrydsm.dsmhackerbe.global.exception.DsmHackerException
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode

class UserNotFoundException : DsmHackerException(ErrorCode.USER_NOT_FOUND)

class UserAlreadyExistException : DsmHackerException(ErrorCode.USER_ALREADY_EXIST)

class PasswordMissMatchException : DsmHackerException(ErrorCode.PASSWORD_MISS_MATCH)
