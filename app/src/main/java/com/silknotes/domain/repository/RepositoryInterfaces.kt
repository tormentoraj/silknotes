package com.silknotes.domain.repository

import com.silknotes.domain.model.Folder
import com.silknotes.domain.model.Note
import com.silknotes.domain.model.NoteWithPages
import com.silknotes.domain.model.Page
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun createFolder(name: String, color: Int, icon: String): Folder
    suspend fun updateFolder(folder: Folder): Boolean
    suspend fun deleteFolder(folderId: Long): Boolean
    suspend fun reorderFolders(folderIds: List<Long>): Boolean
    fun getAllFolders(): Flow<List<Folder>>
    fun getUserFolders(): Flow<List<Folder>>
    suspend fun getFolderById(id: Long): Folder?
    suspend fun getAllNotesFolder(): Folder
    suspend fun getUnfiledFolder(): Folder
    suspend fun getPinnedFolder(): Folder
    suspend fun getNoteCount(folderId: Long): Int
}

interface NoteRepository {
    suspend fun createNote(folderId: Long, title: String): Note
    suspend fun updateNote(note: Note): Boolean
    suspend fun deleteNote(noteId: Long): Boolean
    suspend fun moveNote(noteId: Long, newFolderId: Long): Boolean
    suspend fun setPinned(noteId: Long, pinned: Boolean): Boolean
    suspend fun updateTitle(noteId: Long, title: String): Boolean
    suspend fun updateLastOpened(noteId: Long): Boolean
    fun getNotesByFolder(folderId: Long): Flow<List<Note>>
    fun getPinnedNotes(): Flow<List<Note>>
    fun getAllNotes(): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    suspend fun getNoteById(noteId: Long): Note?
    suspend fun getNoteWithPages(noteId: Long): NoteWithPages?
    suspend fun getNoteCount(folderId: Long): Int
}

interface PageRepository {
    suspend fun createPage(noteId: Long, pageIndex: Int): Page
    suspend fun updatePage(page: Page): Boolean
    suspend fun deletePage(pageId: Long): Boolean
    suspend fun updateStrokes(pageId: Long, strokes: List<com.silknotes.domain.model.Stroke>): Boolean
    suspend fun updateBackgroundColor(pageId: Long, color: Int): Boolean
    fun getPagesByNote(noteId: Long): Flow<List<Page>>
    suspend fun getPage(noteId: Long, pageIndex: Int): Page?
    suspend fun getPageById(pageId: Long): Page?
    suspend fun getPageCount(noteId: Long): Int
}

interface SettingsRepository {
    val themeMode: Flow<Int>
    val gridView: Flow<Int>
    val sortOrder: Flow<Int>
    val showThumbnails: Flow<Boolean>
    val defaultStrokeWidth: Flow<Float>
    val defaultStrokeColor: Flow<Int>
    val defaultTool: Flow<String>
    val pressureSensitivity: Flow<Int>
    val palmRejection: Flow<Boolean>
    val onboardingComplete: Flow<Boolean>
    val autoSaveInterval: Flow<Long>

    suspend fun setThemeMode(mode: Int): Boolean
    suspend fun setGridView(grid: Int): Boolean
    suspend fun setSortOrder(order: Int): Boolean
    suspend fun setShowThumbnails(show: Boolean): Boolean
    suspend fun setDefaultStrokeWidth(width: Float): Boolean
    suspend fun setDefaultStrokeColor(color: Int): Boolean
    suspend fun setDefaultTool(tool: String): Boolean
    suspend fun setPressureSensitivity(sensitivity: Int): Boolean
    suspend fun setPalmRejection(enabled: Boolean): Boolean
    suspend fun setOnboardingComplete(complete: Boolean): Boolean
    suspend fun setAutoSaveInterval(interval: Long): Boolean
}