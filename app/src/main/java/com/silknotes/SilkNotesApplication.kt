package com.silknotes

import android.app.Application
import com.silknotes.data.local.database.SilkNotesDatabase
import com.silknotes.data.local.datastore.SettingsDataStore
import com.silknotes.data.local.repository.FolderRepositoryImpl
import com.silknotes.data.local.repository.NoteRepositoryImpl
import com.silknotes.data.local.repository.PageRepositoryImpl
import com.silknotes.data.local.repository.SettingsRepositoryImpl
import com.silknotes.domain.repository.FolderRepository
import com.silknotes.domain.repository.NoteRepository
import com.silknotes.domain.repository.PageRepository
import com.silknotes.domain.repository.SettingsRepository
import com.silknotes.domain.usecase.folder.*
import com.silknotes.domain.usecase.note.*
import com.silknotes.domain.usecase.page.*
import com.silknotes.domain.usecase.settings.*

class SilkNotesApplication : Application() {

    // Database
    private lateinit var database: SilkNotesDatabase

    // DataStore
    private lateinit var settingsDataStore: SettingsDataStore

    // Repositories
    private lateinit var folderRepository: FolderRepository
    private lateinit var noteRepository: NoteRepository
    private lateinit var pageRepository: PageRepository
    private lateinit var settingsRepository: SettingsRepository

    // Folder UseCases
    lateinit var getAllFoldersUseCase: GetAllFoldersUseCase
    lateinit var getUserFoldersUseCase: GetUserFoldersUseCase
    lateinit var getFolderByIdUseCase: GetFolderByIdUseCase
    lateinit var createFolderUseCase: CreateFolderUseCase
    lateinit var updateFolderUseCase: UpdateFolderUseCase
    lateinit var deleteFolderUseCase: DeleteFolderUseCase
    lateinit var reorderFoldersUseCase: ReorderFoldersUseCase
    lateinit var getAllNotesFolderUseCase: GetAllNotesFolderUseCase
    lateinit var getUnfiledFolderUseCase: GetUnfiledFolderUseCase
    lateinit var getPinnedFolderUseCase: GetPinnedFolderUseCase
    lateinit var getFolderNoteCountUseCase: GetFolderNoteCountUseCase

    // Note UseCases
    lateinit var getNotesByFolderUseCase: GetNotesByFolderUseCase
    lateinit var getPinnedNotesUseCase: GetPinnedNotesUseCase
    lateinit var getAllNotesUseCase: GetAllNotesUseCase
    lateinit var searchNotesUseCase: SearchNotesUseCase
    lateinit var getNoteByIdUseCase: GetNoteByIdUseCase
    lateinit var getNoteWithPagesUseCase: GetNoteWithPagesUseCase
    lateinit var createNoteUseCase: CreateNoteUseCase
    lateinit var updateNoteUseCase: UpdateNoteUseCase
    lateinit var deleteNoteUseCase: DeleteNoteUseCase
    lateinit var moveNoteUseCase: MoveNoteUseCase
    lateinit var setNotePinnedUseCase: SetNotePinnedUseCase
    lateinit var updateNoteTitleUseCase: UpdateNoteTitleUseCase
    lateinit var updateNoteLastOpenedUseCase: UpdateNoteLastOpenedUseCase
    lateinit var getNoteCountUseCase: GetNoteCountUseCase

    // Page UseCases
    lateinit var getPagesByNoteUseCase: GetPagesByNoteUseCase
    lateinit var getPageUseCase: GetPageUseCase
    lateinit var getPageByIdUseCase: GetPageByIdUseCase
    lateinit var createPageUseCase: CreatePageUseCase
    lateinit var updatePageUseCase: UpdatePageUseCase
    lateinit var deletePageUseCase: DeletePageUseCase
    lateinit var updatePageStrokesUseCase: UpdatePageStrokesUseCase
    lateinit var updatePageBackgroundColorUseCase: UpdatePageBackgroundColorUseCase
    lateinit var getPageCountUseCase: GetPageCountUseCase

    // Settings UseCases
    lateinit var getThemeModeUseCase: GetThemeModeUseCase
    lateinit var setThemeModeUseCase: SetThemeModeUseCase
    lateinit var getGridViewUseCase: GetGridViewUseCase
    lateinit var setGridViewUseCase: SetGridViewUseCase
    lateinit var getSortOrderUseCase: GetSortOrderUseCase
    lateinit var setSortOrderUseCase: SetSortOrderUseCase
    lateinit var getShowThumbnailsUseCase: GetShowThumbnailsUseCase
    lateinit var setShowThumbnailsUseCase: SetShowThumbnailsUseCase
    lateinit var getDefaultStrokeWidthUseCase: GetDefaultStrokeWidthUseCase
    lateinit var setDefaultStrokeWidthUseCase: SetDefaultStrokeWidthUseCase
    lateinit var getDefaultStrokeColorUseCase: GetDefaultStrokeColorUseCase
    lateinit var setDefaultStrokeColorUseCase: SetDefaultStrokeColorUseCase
    lateinit var getDefaultToolUseCase: GetDefaultToolUseCase
    lateinit var setDefaultToolUseCase: SetDefaultToolUseCase
    lateinit var getPressureSensitivityUseCase: GetPressureSensitivityUseCase
    lateinit var setPressureSensitivityUseCase: SetPressureSensitivityUseCase
    lateinit var getPalmRejectionUseCase: GetPalmRejectionUseCase
    lateinit var setPalmRejectionUseCase: SetPalmRejectionUseCase
    lateinit var getOnboardingCompleteUseCase: GetOnboardingCompleteUseCase
    lateinit var setOnboardingCompleteUseCase: SetOnboardingCompleteUseCase
    lateinit var getAutoSaveIntervalUseCase: GetAutoSaveIntervalUseCase
    lateinit var setAutoSaveIntervalUseCase: SetAutoSaveIntervalUseCase

    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun getInstance(context: android.content.Context): SilkNotesApplication {
            return context.applicationContext as SilkNotesApplication
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize database
        database = SilkNotesDatabase.getInstance(this)

        // Initialize DataStore
        settingsDataStore = SettingsDataStore.getInstance(this)

        // Initialize repositories
        folderRepository = FolderRepositoryImpl(database.folderDao(), database.noteDao())
        noteRepository = NoteRepositoryImpl(database.noteDao(), database.pageDao())
        pageRepository = PageRepositoryImpl(database.pageDao())
        settingsRepository = SettingsRepositoryImpl(settingsDataStore)

        // Initialize Folder UseCases
        getAllFoldersUseCase = GetAllFoldersUseCase(folderRepository)
        getUserFoldersUseCase = GetUserFoldersUseCase(folderRepository)
        getFolderByIdUseCase = GetFolderByIdUseCase(folderRepository)
        createFolderUseCase = CreateFolderUseCase(folderRepository)
        updateFolderUseCase = UpdateFolderUseCase(folderRepository)
        deleteFolderUseCase = DeleteFolderUseCase(folderRepository)
        reorderFoldersUseCase = ReorderFoldersUseCase(folderRepository)
        getAllNotesFolderUseCase = GetAllNotesFolderUseCase(folderRepository)
        getUnfiledFolderUseCase = GetUnfiledFolderUseCase(folderRepository)
        getPinnedFolderUseCase = GetPinnedFolderUseCase(folderRepository)
        getFolderNoteCountUseCase = GetFolderNoteCountUseCase(folderRepository)

        // Initialize Note UseCases
        getNotesByFolderUseCase = GetNotesByFolderUseCase(noteRepository)
        getPinnedNotesUseCase = GetPinnedNotesUseCase(noteRepository)
        getAllNotesUseCase = GetAllNotesUseCase(noteRepository)
        searchNotesUseCase = SearchNotesUseCase(noteRepository)
        getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository)
        getNoteWithPagesUseCase = GetNoteWithPagesUseCase(noteRepository)
        createNoteUseCase = CreateNoteUseCase(noteRepository)
        updateNoteUseCase = UpdateNoteUseCase(noteRepository)
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
        moveNoteUseCase = MoveNoteUseCase(noteRepository)
        setNotePinnedUseCase = SetNotePinnedUseCase(noteRepository)
        updateNoteTitleUseCase = UpdateNoteTitleUseCase(noteRepository)
        updateNoteLastOpenedUseCase = UpdateNoteLastOpenedUseCase(noteRepository)
        getNoteCountUseCase = GetNoteCountUseCase(noteRepository)

        // Initialize Page UseCases
        getPagesByNoteUseCase = GetPagesByNoteUseCase(pageRepository)
        getPageUseCase = GetPageUseCase(pageRepository)
        getPageByIdUseCase = GetPageByIdUseCase(pageRepository)
        createPageUseCase = CreatePageUseCase(pageRepository)
        updatePageUseCase = UpdatePageUseCase(pageRepository)
        deletePageUseCase = DeletePageUseCase(pageRepository)
        updatePageStrokesUseCase = UpdatePageStrokesUseCase(pageRepository)
        updatePageBackgroundColorUseCase = UpdatePageBackgroundColorUseCase(pageRepository)
        getPageCountUseCase = GetPageCountUseCase(pageRepository)

        // Initialize Settings UseCases
        getThemeModeUseCase = GetThemeModeUseCase(settingsRepository)
        setThemeModeUseCase = SetThemeModeUseCase(settingsRepository)
        getGridViewUseCase = GetGridViewUseCase(settingsRepository)
        setGridViewUseCase = SetGridViewUseCase(settingsRepository)
        getSortOrderUseCase = GetSortOrderUseCase(settingsRepository)
        setSortOrderUseCase = SetSortOrderUseCase(settingsRepository)
        getShowThumbnailsUseCase = GetShowThumbnailsUseCase(settingsRepository)
        setShowThumbnailsUseCase = SetShowThumbnailsUseCase(settingsRepository)
        getDefaultStrokeWidthUseCase = GetDefaultStrokeWidthUseCase(settingsRepository)
        setDefaultStrokeWidthUseCase = SetDefaultStrokeWidthUseCase(settingsRepository)
        getDefaultStrokeColorUseCase = GetDefaultStrokeColorUseCase(settingsRepository)
        setDefaultStrokeColorUseCase = SetDefaultStrokeColorUseCase(settingsRepository)
        getDefaultToolUseCase = GetDefaultToolUseCase(settingsRepository)
        setDefaultToolUseCase = SetDefaultToolUseCase(settingsRepository)
        getPressureSensitivityUseCase = GetPressureSensitivityUseCase(settingsRepository)
        setPressureSensitivityUseCase = SetPressureSensitivityUseCase(settingsRepository)
        getPalmRejectionUseCase = GetPalmRejectionUseCase(settingsRepository)
        setPalmRejectionUseCase = SetPalmRejectionUseCase(settingsRepository)
        getOnboardingCompleteUseCase = GetOnboardingCompleteUseCase(settingsRepository)
        setOnboardingCompleteUseCase = SetOnboardingCompleteUseCase(settingsRepository)
        getAutoSaveIntervalUseCase = GetAutoSaveIntervalUseCase(settingsRepository)
        setAutoSaveIntervalUseCase = SetAutoSaveIntervalUseCase(settingsRepository)

        // Initialize default folders on first launch
        initializeDefaultFolders()
    }

    private fun initializeDefaultFolders() {
        // This will be called from a coroutine in the UI layer to avoid blocking
    }
}