package uz.androdev.memorization.data.db

import androidx.room.TypeConverter
import org.threeten.bp.*

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 9:40 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class Converters {
    @TypeConverter
    fun convertLocalDateTimeToMillis(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    @TypeConverter
    fun convertMillisToLocalDateTime(milliseconds: Long): LocalDateTime {
        return Instant.ofEpochMilli(milliseconds)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    @TypeConverter
    fun convertLocalDateToMillis(localDate: LocalDate): Long {
        return convertLocalDateTimeToMillis(localDate.atStartOfDay())
    }

    @TypeConverter
    fun convertMillisToLocalDate(milliseconds: Long): LocalDate {
        return convertMillisToLocalDateTime(milliseconds).toLocalDate()
    }

    @TypeConverter
    fun convertLocalTimeToNanoOfDay(localTime: LocalTime): Long {
        return localTime.toNanoOfDay()
    }

    @TypeConverter
    fun convertNanoOfDayToLocalTime(nanoOfDay: Long): LocalTime {
        return LocalTime.ofNanoOfDay(nanoOfDay)
    }
}