package hackathon.sprinter.util

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*

const val ASIA_SEOUL = "Asia/Seoul"
fun currentKSTDate(): Date =
    Date(
        OffsetDateTime
            .now(ZoneId.of(ASIA_SEOUL))
            .toInstant()
            .toEpochMilli()
    )

fun convertToKSTDate(utc: Long): OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochSecond(utc), ZoneId.of(ASIA_SEOUL))

fun currentKSTOffsetDateTime(): OffsetDateTime =
    OffsetDateTime.now(ZoneId.of(ASIA_SEOUL))

fun plusKSTDate(year: Long? = 0, month: Long? = 0, day: Long? = 0, min: Long? = 0): Date =
    Date(
        OffsetDateTime.now(ZoneId.of(ASIA_SEOUL))
            .plusYears(year!!)
            .plusMonths(month!!)
            .plusDays(day!!)
            .plusMinutes(min!!)
            .toInstant()
            .toEpochMilli()
    )
