package hs.kr.entrydsm.dsmhackerbe.global.exception.domain.admin

import hs.kr.entrydsm.dsmhackerbe.global.exception.DsmHackerException
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode

class AdminNotFoundException : DsmHackerException(ErrorCode.ADMIN_NOT_FOUND)
