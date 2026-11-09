package com.silknotes.domain.usecase.note

import com.silknotes.domain.model.Note
import com.silknotes.domain.model.NoteWithPages
import com.silknotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesByFolderUseCase(private val repository: NoteRepository) {
    operator fun invoke(folderId: Long): Flow<List<Note>> = repository.getNotesByFolder(folderId)
}

class GetPinnedNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getPinnedNotes()
}

class GetAllNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}

class SearchNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(query: String): Flow<List<Note>> = repository.searchNotes(query)
}

class GetNoteByIdUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(noteId: Long): Note? = repository.getNoteById(noteId)
}

class GetNoteWithPagesUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(noteId: Long): NoteWithPages? = repository.getNoteWithPages(noteId)
}

class CreateNoteUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(folderId: Long, title: String): Note = repository.createNote(folderId, title)
}

class UpdateNoteUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(note: Note): Boolean = repository.updateNote(note)
}

class DeleteNoteUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(noteId: Long): Boolean = repository.deleteNote(noteId)
}

class MoveNoteUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(noteId: Long, newFolderId: Long): Boolean = repository.moveNote(noteId, newFolderId)
}

class SetNotePinnedUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(noteId: Long, pinned: Boolean): Boolean = repository.setPinned(noteId, pinned)
}

class UpdateNoteTitleUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(noteId: Long, title: String): Boolean = repository.updateTitle(noteId, title)
}

class UpdateNoteLastOpenedUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(noteId: Long): Boolean = repository.updateLastOpened(noteId)
}

class GetNoteCountUseCase(private val repository: NoteRepository) {
    operator suspend fun invoke(folderId: Long): Int = repository.getNoteCount(folderId)
}