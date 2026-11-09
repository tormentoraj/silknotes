package com.silknotes.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: FolderEntity): Long

    @Update
    suspend fun update(folder: FolderEntity): Int

    @Query("DELETE FROM folders WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM folders WHERE id = :id")
    suspend fun getById(id: Long): FolderEntity?

    @Query("SELECT * FROM folders WHERE isSystem = 1 AND name = 'All Notes'")
    suspend fun getAllNotesFolder(): FolderEntity?

    @Query("SELECT * FROM folders WHERE isSystem = 1 AND name = 'Unfiled'")
    suspend fun getUnfiledFolder(): FolderEntity?

    @Query("SELECT * FROM folders WHERE isSystem = 1 AND name = 'Pinned'")
    suspend fun getPinnedFolder(): FolderEntity?

    @Query("SELECT * FROM folders ORDER BY isSystem DESC, sortOrder ASC, name ASC")
    fun getAllFolders(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folders WHERE isSystem = 0 ORDER BY sortOrder ASC, name ASC")
    fun getUserFolders(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folders WHERE isSystem = 0 ORDER BY sortOrder ASC, name ASC")
    suspend fun getUserFoldersList(): List<FolderEntity>

    @Query("UPDATE folders SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int): Int

    @Query("SELECT COUNT(*) FROM notes WHERE folderId = :folderId")
    suspend fun getNoteCount(folderId: Long): Int
}