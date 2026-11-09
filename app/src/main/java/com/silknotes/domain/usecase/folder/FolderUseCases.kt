package com.silknotes.domain.usecase.folder

import com.silknotes.domain.model.Folder
import com.silknotes.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow

class GetAllFoldersUseCase(private val repository: FolderRepository) {
    operator fun invoke(): Flow<List<Folder>> = repository.getAllFolders()
}

class GetUserFoldersUseCase(private val repository: FolderRepository) {
    operator fun invoke(): Flow<List<Folder>> = repository.getUserFolders()
}

class GetFolderByIdUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(folderId: Long): Folder? = repository.getFolderById(folderId)
}

class CreateFolderUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(name: String, color: Int, icon: String): Folder =
        repository.createFolder(name, color, icon)
}

class UpdateFolderUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(folder: Folder): Boolean = repository.updateFolder(folder)
}

class DeleteFolderUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(folderId: Long): Boolean = repository.deleteFolder(folderId)
}

class ReorderFoldersUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(folderIds: List<Long>): Boolean = repository.reorderFolders(folderIds)
}

class GetAllNotesFolderUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(): Folder = repository.getAllNotesFolder()
}

class GetUnfiledFolderUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(): Folder = repository.getUnfiledFolder()
}

class GetPinnedFolderUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(): Folder = repository.getPinnedFolder()
}

class GetFolderNoteCountUseCase(private val repository: FolderRepository) {
    operator suspend fun invoke(folderId: Long): Int = repository.getNoteCount(folderId)
}