package com.silknotes.presentation.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silknotes.domain.model.Note
import com.silknotes.domain.model.Page
import com.silknotes.domain.model.Stroke
import com.silknotes.domain.model.ToolType
import com.silknotes.domain.usecase.note.CreateNoteUseCase
import com.silknotes.domain.usecase.note.GetNoteByIdUseCase
import com.silknotes.domain.usecase.note.GetNoteWithPagesUseCase
import com.silknotes.domain.usecase.note.UpdateNoteLastOpenedUseCase
import com.silknotes.domain.usecase.note.UpdateNoteTitleUseCase
import com.silknotes.domain.usecase.page.CreatePageUseCase
import com.silknotes.domain.usecase.page.GetPageCountUseCase
import com.silknotes.domain.usecase.page.GetPageUseCase
import com.silknotes.domain.usecase.page.GetPagesByNoteUseCase
import com.silknotes.domain.usecase.page.UpdatePageBackgroundColorUseCase
import com.silknotes.domain.usecase.page.UpdatePageStrokesUseCase
import com.silknotes.domain.usecase.settings.GetDefaultStrokeColorUseCase
import com.silknotes.domain.usecase.settings.GetDefaultStrokeWidthUseCase
import com.silknotes.domain.usecase.settings.GetDefaultToolUseCase
import com.silknotes.domain.usecase.settings.GetPressureSensitivityUseCase
import com.silknotes.domain.usecase.settings.GetPalmRejectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditorViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createNoteUseCase: CreateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val getNoteWithPagesUseCase: GetNoteWithPagesUseCase,
    private val updateNoteTitleUseCase: UpdateNoteTitleUseCase,
    private val updateNoteLastOpenedUseCase: UpdateNoteLastOpenedUseCase,
    private val createPageUseCase: CreatePageUseCase,
    private val getPageCountUseCase: GetPageCountUseCase,
    private val getPageUseCase: GetPageUseCase,
    private val getPagesByNoteUseCase: GetPagesByNoteUseCase,
    private val updatePageStrokesUseCase: UpdatePageStrokesUseCase,
    private val updatePageBackgroundColorUseCase: UpdatePageBackgroundColorUseCase,
    private val getDefaultStrokeColorUseCase: GetDefaultStrokeColorUseCase,
    private val getDefaultStrokeWidthUseCase: GetDefaultStrokeWidthUseCase,
    private val getDefaultToolUseCase: GetDefaultToolUseCase,
    private val getPressureSensitivityUseCase: GetPressureSensitivityUseCase,
    private val getPalmRejectionUseCase: GetPalmRejectionUseCase
) : ViewModel() {

    private var _noteId = savedStateHandle.get<Long>("noteId") ?: -1L
    private val folderId = savedStateHandle.get<Long>("folderId")

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note

    private val _pages = MutableStateFlow<List<Page>>(emptyList())
    val pages: StateFlow<List<Page>> = _pages

    private val _currentPageIndex = MutableStateFlow(0)
    val currentPageIndex: StateFlow<Int> = _currentPageIndex

    private val _currentPage = MutableStateFlow<Page?>(null)
    val currentPage: StateFlow<Page?> = _currentPage

    private val _strokes = MutableStateFlow<List<Stroke>>(emptyList())
    val strokes: StateFlow<List<Stroke>> = _strokes

    private val _selectedTool = MutableStateFlow<ToolType>(ToolType.PEN)
    val selectedTool: StateFlow<ToolType> = _selectedTool

    private val _strokeColor = MutableStateFlow(0xFF000000)
    val strokeColor: StateFlow<Int> = _strokeColor

    private val _strokeWidth = MutableStateFlow(3f)
    val strokeWidth: StateFlow<Float> = _strokeWidth

    private val _pressureSensitivity = MutableStateFlow(80)
    val pressureSensitivity: StateFlow<Int> = _pressureSensitivity

    private val _palmRejection = MutableStateFlow(true)
    val palmRejection: StateFlow<Boolean> = _palmRejection

    private val _backgroundColor = MutableStateFlow(0xFFFFFFFF)
    val backgroundColor: StateFlow<Int> = _backgroundColor

    private val _undoStack = MutableStateFlow<List<List<Stroke>>>(emptyList())
    val undoStack: StateFlow<List<List<Stroke>>> = _undoStack

    private val _redoStack = MutableStateFlow<List<List<Stroke>>>(emptyList())
    val redoStack: StateFlow<List<List<Stroke>>> = _redoStack

    private val _isNewNote = MutableStateFlow(false)
    val isNewNote: StateFlow<Boolean> = _isNewNote

    init {
        loadSettings()
        loadNote()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _strokeColor.value = getDefaultStrokeColorUseCase().first()
            _strokeWidth.value = getDefaultStrokeWidthUseCase().first()
            val toolStr = getDefaultToolUseCase().first()
            _selectedTool.value = ToolType.values().find { it.id == toolStr } ?: ToolType.PEN
            _pressureSensitivity.value = getPressureSensitivityUseCase().first()
            _palmRejection.value = getPalmRejectionUseCase().first()
        }
    }

    private fun loadNote() {
        viewModelScope.launch {
            if (_noteId > 0) {
                // Existing note
                val note = getNoteByIdUseCase(_noteId)
                _note.value = note
                if (note != null) {
                    updateNoteLastOpenedUseCase(_noteId)
                    loadPages(_noteId)
                }
            } else {
                // New note
                _isNewNote.value = true
                val newNote = createNoteUseCase(folderId ?: 0, "")
                _note.value = newNote
                _noteId = newNote.id
                savedStateHandle.set("noteId", newNote.id)
                loadPages(newNote.id)
            }
        }
    }

    var noteId: Long
        get() = _noteId
        private set(value) { _noteId = value }

    private fun loadPages(noteId: Long) {
        viewModelScope.launch {
            val pageCount = getPageCountUseCase(noteId)
            if (pageCount == 0) {
                val page = createPageUseCase(noteId, 0)
                _pages.value = listOf(page)
                _currentPage.value = page
                _strokes.value = page.strokes
                _backgroundColor.value = page.backgroundColor
            } else {
                val pages = getPagesByNoteUseCase(noteId).first()
                _pages.value = pages
                if (pages.isNotEmpty()) {
                    val page = pages[_currentPageIndex.value.coerceIn(0, pages.size - 1)]
                    _currentPage.value = page
                    _strokes.value = page.strokes
                    _backgroundColor.value = page.backgroundColor
                }
            }
        }
    }

    fun setStrokes(newStrokes: List<Stroke>) {
        val current = _strokes.value
        if (current != newStrokes) {
            // Push to undo stack
            _undoStack.value = _undoStack.value + current
            // Limit undo stack size
            if (_undoStack.value.size > 50) {
                _undoStack.value = _undoStack.value.drop(1)
            }
            // Clear redo stack on new action
            _redoStack.value = emptyList()
            _strokes.value = newStrokes
            saveStrokes()
        }
    }

    fun addStroke(stroke: Stroke) {
        val newStrokes = _strokes.value + stroke
        setStrokes(newStrokes)
    }

    fun undo() {
        val undoStack = _undoStack.value
        if (undoStack.isNotEmpty()) {
            val lastStrokes = undoStack.last()
            _redoStack.value = _redoStack.value + _strokes.value
            _undoStack.value = undoStack.dropLast(1)
            _strokes.value = lastStrokes
            saveStrokes()
        }
    }

    fun redo() {
        val redoStack = _redoStack.value
        if (redoStack.isNotEmpty()) {
            val nextStrokes = redoStack.last()
            _undoStack.value = _undoStack.value + _strokes.value
            _redoStack.value = redoStack.dropLast(1)
            _strokes.value = nextStrokes
            saveStrokes()
        }
    }

    fun canUndo(): Boolean = _undoStack.value.isNotEmpty()
    fun canRedo(): Boolean = _redoStack.value.isNotEmpty()

    fun setSelectedTool(tool: ToolType) {
        _selectedTool.value = tool
    }

    fun setStrokeColor(color: Int) {
        _strokeColor.value = color
    }

    fun setStrokeWidth(width: Float) {
        _strokeWidth.value = width
    }

    fun setBackgroundColor(color: Int) {
        _backgroundColor.value = color
        saveBackgroundColor()
    }

    fun nextPage() {
        if (_currentPageIndex.value < _pages.value.size - 1) {
            _currentPageIndex.value++
            updateCurrentPage()
        }
    }

    fun previousPage() {
        if (_currentPageIndex.value > 0) {
            _currentPageIndex.value--
            updateCurrentPage()
        }
    }

    fun addPage() {
        viewModelScope.launch {
            val newIndex = _pages.value.size
            val page = createPageUseCase(noteId, newIndex)
            _pages.value = _pages.value + page
            _currentPageIndex.value = newIndex
            updateCurrentPage()
        }
    }

    fun deletePage(index: Int) {
        if (_pages.value.size <= 1) return
        viewModelScope.launch {
            val pages = _pages.value.toMutableList()
            pages.removeAt(index)
            _pages.value = pages
            if (_currentPageIndex.value >= pages.size) {
                _currentPageIndex.value = pages.size - 1
            }
            updateCurrentPage()
        }
    }

    fun updateNoteTitle(title: String) {
        _note.value?.let { note ->
            updateNoteTitleUseCase(note.id, title)
            _note.value = note.copy(title = title)
        }
    }

    private fun updateCurrentPage() {
        val pages = _pages.value
        if (pages.isNotEmpty() && _currentPageIndex.value < pages.size) {
            val page = pages[_currentPageIndex.value]
            _currentPage.value = page
            _strokes.value = page.strokes
            _backgroundColor.value = page.backgroundColor
            _undoStack.value = emptyList()
            _redoStack.value = emptyList()
        }
    }

    private fun saveStrokes() {
        val page = _currentPage.value
        page?.let {
            updatePageStrokesUseCase(page.id, _strokes.value)
        }
    }

    private fun saveBackgroundColor() {
        val page = _currentPage.value
        page?.let {
            updatePageBackgroundColorUseCase(page.id, _backgroundColor.value)
        }
    }
}