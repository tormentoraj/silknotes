package com.silknotes.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.silknotes.data.local.database.converters.ColorConverter
import com.silknotes.data.local.database.converters.FolderIconConverter
import com.silknotes.data.local.database.converters.InstantConverter
import com.silknotes.data.local.database.converters.StrokeListConverter
import java.time.Instant

@Entity(tableName = "folders")
@TypeConverters(InstantConverter::class, ColorConverter::class, FolderIconConverter::class)
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val color: Int = 0xFF6750A4.toInt(),
    val icon: String = "folder",
    val sortOrder: Int = 0,
    val isSystem: Boolean = false,
    @TypeConverters(InstantConverter::class) val createdAt: Instant = Instant.EPOCH,
    @TypeConverters(InstantConverter::class) val updatedAt: Instant = Instant.EPOCH
)

@Entity(tableName = "notes")
@TypeConverters(InstantConverter::class)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val folderId: Long,
    val title: String = "",
    val isPinned: Boolean = false,
    val pageCount: Int = 1,
    @TypeConverters(InstantConverter::class) val createdAt: Instant = Instant.EPOCH,
    @TypeConverters(InstantConverter::class) val updatedAt: Instant = Instant.EPOCH,
    @TypeConverters(InstantConverter::class) val lastOpenedAt: Instant? = null
)

@Entity(tableName = "pages")
@TypeConverters(InstantConverter::class, StrokeListConverter::class)
data class PageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val noteId: Long,
    val pageIndex: Int,
    val backgroundColor: Int = 0xFFFFFFFF.toInt(),
    val strokes: List<StrokeEntity> = emptyList(),
    @TypeConverters(InstantConverter::class) val createdAt: Instant = Instant.EPOCH,
    @TypeConverters(InstantConverter::class) val updatedAt: Instant = Instant.EPOCH
)

data class StrokeEntity(
    val points: List<StrokePoint>,
    val color: Int,
    val strokeWidth: Float,
    val toolType: String,
    val pressureSensitive: Boolean = true
)

data class StrokePoint(
    val x: Float,
    val y: Float,
    val pressure: Float = 1.0f,
    val timestamp: Long = System.currentTimeMillis()
)