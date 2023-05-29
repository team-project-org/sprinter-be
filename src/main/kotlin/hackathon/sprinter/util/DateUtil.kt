package hackathon.sprinter.util

import com.netflix.dgs.codegen.generated.types.Member
import com.netflix.dgs.codegen.generated.types.Post
import hackathon.sprinter.post.model.MemberDto
import hackathon.sprinter.post.model.PostDto
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*

fun currentKSTDate(): Date =
    Date(
        OffsetDateTime
            .now(ZoneId.of("Asia/Seoul"))
            .toInstant()
            .toEpochMilli()
    )

fun plusKSTDate(year: Long? = 0, month: Long? = 0, day: Long? = 0, min: Long? = 0): Date =
    Date(
        OffsetDateTime.now(ZoneId.of("Asia/Seoul"))
            .plusYears(year!!)
            .plusMonths(month!!)
            .plusDays(day!!)
            .plusMinutes(min!!)
            .toInstant()
            .toEpochMilli()
    )
