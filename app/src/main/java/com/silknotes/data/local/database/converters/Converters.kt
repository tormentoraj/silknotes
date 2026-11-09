package com.silknotes.data.local.database.converters

import androidx.room.TypeConverter
import com.silknotes.data.local.database.StrokeEntity
import com.silknotes.data.local.database.StrokePoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

private val json = Json { ignoreUnknownKeys = true }

object InstantConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun toTimestamp(instant: Instant?): Long? = instant?.toEpochMilli()
}

object ColorConverter {
    @TypeConverter
    fun toInt(value: Int): Int = value
}

object FolderIconConverter {
    @TypeConverter
    fun toString(value: String): String = value
}

object StrokeListConverter {
    @TypeConverter
    fun fromJson(json: String?): List<StrokeEntity> = json?.let {
        Json.decodeFromString(it)
    } ?: emptyList()

    @TypeConverter
    fun toJson(strokes: List<StrokeEntity>): String = Json.encodeToString(strokes)
}

object StrokePointListConverter {
    @TypeConverter
    fun fromJson(json: String?): List<StrokePoint> = json?.let {
        Json.decodeFromString(it)
    } ?: emptyList()

    @TypeConverter
    fun toJson(points: List<StrokePoint>): String = Json.encodeToString(points)
}