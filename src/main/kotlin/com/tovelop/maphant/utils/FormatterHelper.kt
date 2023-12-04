package com.tovelop.maphant.utils

import java.time.LocalDateTime
import java.time.ZoneOffset

class FormatterHelper {
    companion object {
        fun LocalDateTime.formatTime(): String {
            val now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            val secDiff = now - this.toEpochSecond(ZoneOffset.UTC);

            if (secDiff < 60) {
                return "방금 전"
            }

            val diffMin = secDiff / 60
            if (diffMin < 60)
                return "${diffMin}분 전"

            val diffHour = diffMin / 60
            if (diffHour < 24)
                return "${diffHour}시간 전"

            val diffDay = diffHour / 24;
            if (diffDay < 7)
                return "${diffDay}일 전";

            val diffWeek = diffDay / 7;
            if (diffWeek < 4)
                return "${diffWeek}주 전";

            val diffMonth = diffWeek / 4;
            if (diffMonth < 12)
                return "${diffMonth}달 전";

            val diffYear = diffMonth / 12;
            if (diffYear < 1000)
                return "${diffYear}년 전";

            return "오래 전"
        }
    }
}