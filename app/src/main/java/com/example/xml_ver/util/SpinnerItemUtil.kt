package com.example.xml_ver.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

val personList = listOf("0명","1명", "2명", "3명", "4명", "5명 이상")

val categoryList = listOf("모임 카테고리","친구", "술", "❤️ (Love)", "번개모임", "공부", "스터디", "공모전")

@RequiresApi(Build.VERSION_CODES.O)
val yearList = listOf("${LocalDateTime.now().year}년", "${LocalDateTime.now().year + 1}년")

val monthList = (1..12).map { "${it}월" }

val dayList = (1..31).map { "${it}일" }

val hourList = (0..23).map { "%02d시".format(it) }

val reportList = listOf("부적절한 말(욕설/혐오/음란)", "허위 사실 유포", "채팅장 도배 및 광고", "기타")

val inquiryTypeList = listOf("이용 문의", "버그 신고", "기능 추가 요청", "기타 문의")

val noticeTypeList = listOf("안내", "공지", "업데이트")

val postManagementList = listOf("임시 마감", "수정하기", "삭제하기")