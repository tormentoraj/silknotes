package com.silknotes.domain.usecase.page

import com.silknotes.domain.model.Page
import com.silknotes.domain.model.Stroke
import com.silknotes.domain.repository.PageRepository
import kotlinx.coroutines.flow.Flow

class GetPagesByNoteUseCase(private val repository: PageRepository) {
    operator fun invoke(noteId: Long): Flow<List<Page>> = repository.getPagesByNote(noteId)
}

class GetPageUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(noteId: Long, pageIndex: Int): Page? = repository.getPage(noteId, pageIndex)
}

class GetPageByIdUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(pageId: Long): Page? = repository.getPageById(pageId)
}

class CreatePageUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(noteId: Long, pageIndex: Int): Page = repository.createPage(noteId, pageIndex)
}

class UpdatePageUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(page: Page): Boolean = repository.updatePage(page)
}

class DeletePageUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(pageId: Long): Boolean = repository.deletePage(pageId)
}

class UpdatePageStrokesUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(pageId: Long, strokes: List<Stroke>): Boolean = repository.updateStrokes(pageId, strokes)
}

class UpdatePageBackgroundColorUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(pageId: Long, color: Int): Boolean = repository.updateBackgroundColor(pageId, color)
}

class GetPageCountUseCase(private val repository: PageRepository) {
    operator suspend fun invoke(noteId: Long): Int = repository.getPageCount(noteId)
}