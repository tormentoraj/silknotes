package com.silknotes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silknotes.domain.model.Folder
import com.silknotes.domain.model.Note
import com.silknotes.domain.usecase.folder.GetAllFoldersUseCase
import com.silknotes.domain.usecase.note.GetAllNotesUseCase
import com.silknotes.domain.usecase.note.GetNotesByFolderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNotesByFolderUseCase: GetNotesByFolderUseCase
) : ViewModel() {

    private val _selectedFolderId = MutableStateFlow<Long?>(null)
    val selectedFolderId = _selectedFolderId

    val folders = getAllFoldersUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val notes = combine(
        _selectedFolderId.distinctUntilChanged()
    ) { folderId ->
        if (folderId == null) {
            getAllNotesUseCase()
        } else {
            getNotesByFolderUseCase(folderId)
        }
    }.flattenMerge().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun selectFolder(folderId: Long?) {
        _selectedFolderId.value = folderId
    }
}