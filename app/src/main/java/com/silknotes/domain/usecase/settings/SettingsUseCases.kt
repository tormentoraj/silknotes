package com.silknotes.domain.usecase.settings

import com.silknotes.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeModeUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Int> = repository.themeMode
}

class SetThemeModeUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(mode: Int): Boolean = repository.setThemeMode(mode)
}

class GetGridViewUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Int> = repository.gridView
}

class SetGridViewUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(grid: Int): Boolean = repository.setGridView(grid)
}

class GetSortOrderUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Int> = repository.sortOrder
}

class SetSortOrderUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(order: Int): Boolean = repository.setSortOrder(order)
}

class GetShowThumbnailsUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = repository.showThumbnails
}

class SetShowThumbnailsUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(show: Boolean): Boolean = repository.setShowThumbnails(show)
}

class GetDefaultStrokeWidthUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Float> = repository.defaultStrokeWidth
}

class SetDefaultStrokeWidthUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(width: Float): Boolean = repository.setDefaultStrokeWidth(width)
}

class GetDefaultStrokeColorUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Int> = repository.defaultStrokeColor
}

class SetDefaultStrokeColorUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(color: Int): Boolean = repository.setDefaultStrokeColor(color)
}

class GetDefaultToolUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<String> = repository.defaultTool
}

class SetDefaultToolUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(tool: String): Boolean = repository.setDefaultTool(tool)
}

class GetPressureSensitivityUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Int> = repository.pressureSensitivity
}

class SetPressureSensitivityUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(sensitivity: Int): Boolean = repository.setPressureSensitivity(sensitivity)
}

class GetPalmRejectionUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = repository.palmRejection
}

class SetPalmRejectionUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(enabled: Boolean): Boolean = repository.setPalmRejection(enabled)
}

class GetOnboardingCompleteUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = repository.onboardingComplete
}

class SetOnboardingCompleteUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(complete: Boolean): Boolean = repository.setOnboardingComplete(complete)
}

class GetAutoSaveIntervalUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Long> = repository.autoSaveInterval
}

class SetAutoSaveIntervalUseCase(private val repository: SettingsRepository) {
    operator suspend fun invoke(interval: Long): Boolean = repository.setAutoSaveInterval(interval)
}