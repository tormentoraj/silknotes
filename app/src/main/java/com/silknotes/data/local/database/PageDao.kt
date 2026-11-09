package com.silknotes.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(page: PageEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(pages: List<PageEntity>): List<Long>

    @Update
    suspend fun update(page: PageEntity): Int

    @Query("DELETE FROM pages WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("DELETE FROM pages WHERE noteId = :noteId")
    suspend fun deleteByNoteId(noteId: Long): Int

    @Query("SELECT * FROM pages WHERE id = :id")
    suspend fun getById(id: Long): PageEntity?

    @Query("SELECT * FROM pages WHERE noteId = :noteId ORDER BY pageIndex ASC")
    fun getPagesByNote(noteId: Long): Flow<List<PageEntity>>

    @Query("SELECT * FROM pages WHERE noteId = :noteId ORDER BY pageIndex ASC")
    suspend fun getPagesByNoteList(noteId: Long): List<PageEntity>

    @Query("SELECT * FROM pages WHERE noteId = :noteId AND pageIndex = :pageIndex")
    suspend fun getPage(noteId: Long, pageIndex: Int): PageEntity?

    @Query("SELECT COUNT(*) FROM pages WHERE noteId = :noteId")
    suspend fun getPageCount(noteId: Long): Int

    @Query("UPDATE pages SET strokes = :strokes, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStrokes(id: Long, strokes: List<StrokeEntity>, updatedAt: Long): Int

    @Query("UPDATE pages SET backgroundColor = :color, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateBackgroundColor(id: Long, color: Int, updatedAt: Long): Int
}