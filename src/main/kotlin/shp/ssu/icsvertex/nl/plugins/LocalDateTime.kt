package shp.ssu.icsvertex.nl.plugins

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


fun convertToLocalDateTimeViaInstant(dateToConvert: Date): LocalDateTime {
    return dateToConvert.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}