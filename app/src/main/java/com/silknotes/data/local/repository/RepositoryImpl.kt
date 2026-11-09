package com.silknotes.data.local.repository

import com.silknotes.data.local.database.FolderDao
import com.silknotes.data.local.database.NoteDao
import com.silknotes.data.local.database.PageDao
import com.silknotes.data.local.database.SilkNotesDatabase
import com.silknotes.data.local.database.FolderEntity
import com.silknotes.data.local.database.NoteEntity
import com.silknotes.data.local.database.PageEntity
import com.silknotes.data.local.database.StrokeEntity
import com.silknotes.domain.model.Folder
import com.silknotes.domain.model.Note
import com.silknotes.domain.model.NoteWithPages
import com.silknotes.domain.model.Page
import com.silknotes.domain.model.Stroke
import com.silknotes.domain.model.StrokePoint
import com.silknotes.domain.repository.FolderRepository
import com.silknotes.domain.repository.NoteRepository
import com.silknotes.domain.repository.PageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class FolderRepositoryImpl(
    private val folderDao: FolderDao,
    private val noteDao: NoteDao
) : FolderRepository {

    override suspend fun createFolder(name: String, color: Int, icon: String): Folder {
        val maxOrder = folderDao.getUserFoldersList().maxByOrNull { it.sortOrder }?.sortOrder ?: 0
        val entity = FolderEntity(
            name = name,
            color = color,
            icon = icon,
            sortOrder = maxOrder + 1
        )
        val id = folderDao.insert(entity)
        return getFolderById(id)!!
    }

    override suspend fun updateFolder(folder: Folder): Boolean {
        val entity = folderDao.getById(folder.id)?.copy(
            name = folder.name,
            color = folder.color,
            icon = folder.icon,
            sortOrder = folder.sortOrder,
            updatedAt = Instant.now()
        )
        return entity != null && folderDao.update(entity) > 0
    }

    override suspend fun deleteFolder(folderId: Long): Boolean {
        val unfiledFolder = folderDao.getUnfiledFolder()
        if (unfiledFolder != null) {
            // Move notes to Unfiled folder
            val notes = noteDao.getNotesByFolderList(folderId)
            for (note in notes) {
                noteDao.moveToFolder(note.id, unfiledFolder.id)
            }
        }
        return folderDao.delete(folderId) > 0
    }

    override suspend fun reorderFolders(folderIds: List<Long>): Boolean {
        for (i in folderIds.indices) {
            folderDao.updateSortOrder(folderIds[i], i)
        }
        return true
    }

    override fun getAllFolders(): Flow<List<Folder>> {
        return folderDao.getAllFolders().map { entities ->
            entities.map { entityToFolder(it) }
        }
    }

    override fun getUserFolders(): Flow<List<Folder>> {
        return folderDao.getUserFolders().map { entities ->
            entities.map { entityToFolder(it) }
        }
    }

    override suspend fun getFolderById(id: Long): Folder? {
        return folderDao.getById(id)?.let { entityToFolder(it) }
    }

    override suspend fun getAllNotesFolder(): Folder {
        return folderDao.getAllNotesFolder()?.let { entityToFolder(it) }!!
    }

    override suspend fun getUnfiledFolder(): Folder {
        return folderDao.getUnfiledFolder()?.let { entityToFolder(it) }!!
    }

    override suspend fun getPinnedFolder(): Folder {
        return folderDao.getPinnedFolder()?.let { entityToFolder(it) }!!
    }

    override suspend fun getNoteCount(folderId: Long): Int {
        return folderDao.getNoteCount(folderId)
    }

    private fun entityToFolder(entity: FolderEntity): Folder {
        return Folder(
            id = entity.id,
            name = entity.name,
            color = entity.color,
            icon = entity.icon,
            sortOrder = entity.sortOrder,
            isSystem = entity.isSystem,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            noteCount = 0 // Will be updated by the ViewModel
        )
    }
}

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val pageDao: PageDao
) : NoteRepository {

    override suspend fun createNote(folderId: Long, title: String): Note {
        val entity = NoteEntity(
            folderId = folderId,
            title = title,
            pageCount = 1
        )
        val id = noteDao.insert(entity)
        // Create first page
        pageDao.insert(PageEntity(noteId = id, pageIndex = 0))
        return getNoteById(id)!!
    }

    override suspend fun updateNote(note: Note): Boolean {
        val entity = noteDao.getById(note.id)?.copy(
            folderId = note.folderId,
            title = note.title,
            isPinned = note.isPinned,
            pageCount = note.pageCount,
            updatedAt = note.updatedAt,
            lastOpenedAt = note.lastOpenedAt
        )
        return entity != null && noteDao.update(entity) > 0
    }

    override suspend fun deleteNote(noteId: Long): Boolean {
        pageDao.deleteByNoteId(noteId)
        return noteDao.delete(noteId) > 0
    }

    override suspend fun moveNote(noteId: Long, newFolderId: Long): Boolean {
        return noteDao.moveToFolder(noteId, newFolderId) > 0
    }

    override suspend fun setPinned(noteId: Long, pinned: Boolean): Boolean {
        return noteDao.setPinned(noteId, pinned) > 0
    }

    override suspend fun updateTitle(noteId: Long, title: String): Boolean {
        return noteDao.updateTitle(noteId, title, Instant.now().toEpochMilli()) > 0
    }

    override suspend fun updateLastOpened(noteId: Long): Boolean {
        return noteDao.updateLastOpened(noteId, Instant.now().toEpochMilli()) > 0
    }

    override fun getNotesByFolder(folderId: Long): Flow<List<Note>> {
        return noteDao.getNotesByFolder(folderId).map { entities ->
            entities.map { entityToNote(it) }
        }
    }

    override fun getPinnedNotes(): Flow<List<Note>> {
        return noteDao.getPinnedNotes().map { entities ->
            entities.map { entityToNote(it) }
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { entityToNote(it) }
        }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes("%$query%").map { entities ->
            entities.map { entityToNote(it) }
        }
    }

    override suspend fun getNoteById(noteId: Long): Note? {
        return noteDao.getById(noteId)?.let { entityToNote(it) }
    }

    override suspend fun getNoteWithPages(noteId: Long): NoteWithPages? {
        val note = noteDao.getById(noteId)?.let { entityToNote(it) } ?: return null
        val pages = pageDao.getPagesByNoteList(noteId).map { entityToPage(it) }
        return NoteWithPages(note, pages)
    }

    override suspend fun getNoteCount(folderId: Long): Int {
        return noteDao.getNoteCount(folderId)
    }

    private fun entityToNote(entity: NoteEntity): Note {
        return Note(
            id = entity.id,
            folderId = entity.folderId,
            title = entity.title,
            isPinned = entity.isPinned,
            pageCount = entity.pageCount,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            lastOpenedAt = entity.lastOpenedAt
        )
    }

    private fun entityToPage(entity: PageEntity): Page {
        return Page(
            id = entity.id,
            noteId = entity.noteId,
            pageIndex = entity.pageIndex,
            backgroundColor = entity.backgroundColor,
            strokes = entity.strokes.map { strokeEntityToStroke(it) },
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    private fun strokeEntityToStroke(entity: StrokeEntity): Stroke {
        return Stroke(
            points = entity.points.map { StrokePoint(it.x, it.y, it.pressure, it.timestamp) },
            color = entity.color,
            strokeWidth = entity.strokeWidth,
            toolType = entity.toolType,
            pressureSensitive = entity.pressureSensitive
        )
    }
}

class PageRepositoryImpl(
    private val pageDao: PageDao
) : PageRepository {

    override suspend fun createPage(noteId: Long, pageIndex: Int): Page {
        val entity = PageEntity(noteId = noteId, pageIndex = pageIndex)
        val id = pageDao.insert(entity)
        return getPageById(id)!!
    }

    override suspend fun updatePage(page: Page): Boolean {
        val entity = pageDao.getById(page.id)?.copy(
            noteId = page.noteId,
            pageIndex = page.pageIndex,
            backgroundColor = page.backgroundColor,
            strokes = page.strokes.map { strokeToEntity(it) },
            updatedAt = page.updatedAt
        )
        return entity != null && pageDao.update(entity) > 0
    }

    override suspend fun deletePage(pageId: Long): Boolean {
        return pageDao.delete(pageId) > 0
    }

    override suspend fun updateStrokes(pageId: Long, strokes: List<Stroke>): Boolean {
        return pageDao.updateStrokes(pageId, strokes.map { strokeToEntity(it) }, Instant.now().toEpochMilli()) > 0
    }

    override suspend fun updateBackgroundColor(pageId: Long, color: Int): Boolean {
        return pageDao.updateBackgroundColor(pageId, color, Instant.now().toEpochMilli()) > 0
    }

    override fun getPagesByNote(noteId: Long): Flow<List<Page>> {
        return pageDao.getPagesByNote(noteId).map { entities ->
            entities.map { entityToPage(it) }
        }
    }

    override suspend fun getPage(noteId: Long, pageIndex: Int): Page? {
        return pageDao.getPage(noteId, pageIndex)?.let { entityToPage(it) }
    }

    override suspend fun getPageById(pageId: Long): Page? {
        return pageDao.getById(pageId)?.let { entityToPage(it) }
    }

    override suspend fun getPageCount(noteId: Long): Int {
        return pageDao.getPageCount(noteId)
    }

    private fun entityToPage(entity: PageEntity): Page {
        return Page(
            id = entity.id,
            noteId = entity.noteId,
            pageIndex = entity.pageIndex,
            backgroundColor = entity.backgroundColor,
            strokes = entity.strokes.map { strokeEntityToStroke(it) },
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    private fun strokeEntityToStroke(entity: StrokeEntity): Stroke {
        return Stroke(
            points = entity.points.map { StrokePoint(it.x, it.y, it.pressure, it.timestamp) },
            color = entity.color,
            strokeWidth = entity.strokeWidth,
            toolType = entity.toolType,
            pressureSensitive = entity.pressureSensitive
        )
    }

    private fun strokeToEntity(stroke: Stroke): StrokeEntity {
        return StrokeEntity(
            points = stroke.points.map { com.silknotes.data.local.database.StrokePoint(it.x, it.y, it.pressure, it.timestamp) },
            color = stroke.color,
            strokeWidth = stroke.strokeWidth,
            toolType = stroke.toolType,
            pressureSensitive = stroke.pressureSensitive
        )
    }
}