package jp.numero.dagashiapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val JST: ZoneId = ZoneId.of("Asia/Tokyo")

@Composable
fun dateTimeString(
    instant: Instant,
    format: String,
    locale: Locale = Locale.getDefault(),
    zoneId: ZoneId = JST,
): String = remember(instant, format, locale, zoneId) {
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    DateTimeFormatter.ofPattern(format, locale).format(zonedDateTime)
}