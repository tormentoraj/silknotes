package com.silknotes.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity): Int

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Long): NoteEntity?

    @Query("SELECT * FROM notes WHERE folderId = :folderId ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesByFolder(folderId: Long): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE folderId = :folderId ORDER BY isPinned DESC, updatedAt DESC")
    suspend fun getNotesByFolderList(folderId: Long): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE isPinned = 1 ORDER BY updatedAt DESC")
    fun getPinnedNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE :query OR folderId IN (SELECT id FROM folders WHERE name LIKE :query) ORDER BY isPinned DESC, updatedAt DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Query("UPDATE notes SET folderId = :newFolderId WHERE id = :noteId")
    suspend fun moveToFolder(noteId: Long, newFolderId: Long): Int

    @Query("UPDATE notes SET isPinned = :pinned WHERE id = :id")
    suspend fun setPinned(id: Long, pinned: Boolean): Int

    @Query("UPDATE notes SET title = :title, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateTitle(id: Long, title: String, updatedAt: Long): Int

    @Query("UPDATE notes SET pageCount = :pageCount, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updatePageCount(id: Long, pageCount: Int, updatedAt: Long): Int

    @Query("UPDATE notes SET lastOpenedAt = :lastOpenedAt WHERE id = :id")
    suspend fun updateLastOpened(id: Long, lastOpenedAt: Long): Int

    @Query("SELECT COUNT(*) FROM notes WHERE folderId = :folderId")
    suspend fun getNoteCount(folderId: Long): Int
}