package com.w4eret1ckrtb1tch.app38.db.room

import androidx.room.TypeConverter
import java.util.*


class TypeConverter {

    @TypeConverter
    fun fromLongToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: Date?):Long?{
        return date?.time
    }
}