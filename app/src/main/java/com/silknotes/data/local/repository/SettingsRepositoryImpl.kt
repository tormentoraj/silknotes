package com.silknotes.data.local.repository

import com.silknotes.data.local.datastore.SettingsDataStore
import com.silknotes.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {

    override val themeMode: Flow<Int> = settingsDataStore.themeMode
    override val gridView: Flow<Int> = settingsDataStore.gridView
    override val sortOrder: Flow<Int> = settingsDataStore.sortOrder
    override val showThumbnails: Flow<Boolean> = settingsDataStore.showThumbnails
    override val defaultStrokeWidth: Flow<Float> = settingsDataStore.defaultStrokeWidth
    override val defaultStrokeColor: Flow<Int> = settingsDataStore.defaultStrokeColor
    override val defaultTool: Flow<String> = settingsDataStore.defaultTool
    override val pressureSensitivity: Flow<Int> = settingsDataStore.pressureSensitivity
    override val palmRejection: Flow<Boolean> = settingsDataStore.palmRejection
    override val onboardingComplete: Flow<Boolean> = settingsDataStore.onboardingComplete
    override val autoSaveInterval: Flow<Long> = settingsDataStore.autoSaveInterval

    override suspend fun setThemeMode(mode: Int): Boolean = settingsDataStore.setThemeMode(mode)
    override suspend fun setGridView(grid: Int): Boolean = settingsDataStore.setGridView(grid)
    override suspend fun setSortOrder(order: Int): Boolean = settingsDataStore.setSortOrder(order)
    override suspend fun setShowThumbnails(show: Boolean): Boolean = settingsDataStore.setShowThumbnails(show)
    override suspend fun setDefaultStrokeWidth(width: Float): Boolean = settingsDataStore.setDefaultStrokeWidth(width)
    override suspend fun setDefaultStrokeColor(color: Int): Boolean = settingsDataStore.setDefaultStrokeColor(color)
    override suspend fun setDefaultTool(tool: String): Boolean = settingsDataStore.setDefaultTool(tool)
    override suspend fun setPressureSensitivity(sensitivity: Int): Boolean = settingsDataStore.setPressureSensitivity(sensitivity)
    override suspend fun setPalmRejection(enabled: Boolean): Boolean = settingsDataStore.setPalmRejection(enabled)
    override suspend fun setOnboardingComplete(complete: Boolean): Boolean = settingsDataStore.setOnboardingComplete(complete)
    override suspend fun setAutoSaveInterval(interval: Long): Boolean = settingsDataStore.setAutoSaveInterval(interval)
}