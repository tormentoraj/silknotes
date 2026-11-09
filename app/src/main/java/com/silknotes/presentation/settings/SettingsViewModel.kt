package com.silknotes.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silknotes.domain.model.ThemeMode
import com.silknotes.domain.usecase.settings.GetAutoSaveIntervalUseCase
import com.silknotes.domain.usecase.settings.GetDefaultStrokeColorUseCase
import com.silknotes.domain.usecase.settings.GetDefaultStrokeWidthUseCase
import com.silknotes.domain.usecase.settings.GetDefaultToolUseCase
import com.silknotes.domain.usecase.settings.GetGridViewUseCase
import com.silknotes.domain.usecase.settings.GetOnboardingCompleteUseCase
import com.silknotes.domain.usecase.settings.GetPalmRejectionUseCase
import com.silknotes.domain.usecase.settings.GetPressureSensitivityUseCase
import com.silknotes.domain.usecase.settings.GetShowThumbnailsUseCase
import com.silknotes.domain.usecase.settings.GetSortOrderUseCase
import com.silknotes.domain.usecase.settings.GetThemeModeUseCase
import com.silknotes.domain.usecase.settings.SetAutoSaveIntervalUseCase
import com.silknotes.domain.usecase.settings.SetDefaultStrokeColorUseCase
import com.silknotes.domain.usecase.settings.SetDefaultStrokeWidthUseCase
import com.silknotes.domain.usecase.settings.SetDefaultToolUseCase
import com.silknotes.domain.usecase.settings.SetGridViewUseCase
import com.silknotes.domain.usecase.settings.SetOnboardingCompleteUseCase
import com.silknotes.domain.usecase.settings.SetPalmRejectionUseCase
import com.silknotes.domain.usecase.settings.SetPressureSensitivityUseCase
import com.silknotes.domain.usecase.settings.SetShowThumbnailsUseCase
import com.silknotes.domain.usecase.settings.SetSortOrderUseCase
import com.silknotes.domain.usecase.settings.SetThemeModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val getGridViewUseCase: GetGridViewUseCase,
    private val setGridViewUseCase: SetGridViewUseCase,
    private val getSortOrderUseCase: GetSortOrderUseCase,
    private val setSortOrderUseCase: SetSortOrderUseCase,
    private val getShowThumbnailsUseCase: GetShowThumbnailsUseCase,
    private val setShowThumbnailsUseCase: SetShowThumbnailsUseCase,
    private val getDefaultStrokeWidthUseCase: GetDefaultStrokeWidthUseCase,
    private val setDefaultStrokeWidthUseCase: SetDefaultStrokeWidthUseCase,
    private val getDefaultStrokeColorUseCase: GetDefaultStrokeColorUseCase,
    private val setDefaultStrokeColorUseCase: SetDefaultStrokeColorUseCase,
    private val getDefaultToolUseCase: GetDefaultToolUseCase,
    private val setDefaultToolUseCase: SetDefaultToolUseCase,
    private val getPressureSensitivityUseCase: GetPressureSensitivityUseCase,
    private val setPressureSensitivityUseCase: SetPressureSensitivityUseCase,
    private val getPalmRejectionUseCase: GetPalmRejectionUseCase,
    private val setPalmRejectionUseCase: SetPalmRejectionUseCase,
    private val getOnboardingCompleteUseCase: GetOnboardingCompleteUseCase,
    private val setOnboardingCompleteUseCase: SetOnboardingCompleteUseCase,
    private val getAutoSaveIntervalUseCase: GetAutoSaveIntervalUseCase,
    private val setAutoSaveIntervalUseCase: SetAutoSaveIntervalUseCase
) : ViewModel() {

    val themeMode = getThemeModeUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ThemeMode.SYSTEM.id
    )

    val gridView = getGridViewUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        1
    )

    val sortOrder = getSortOrderUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        0
    )

    val showThumbnails = getShowThumbnailsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        true
    )

    val defaultStrokeWidth = getDefaultStrokeWidthUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        3f
    )

    val defaultStrokeColor = getDefaultStrokeColorUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        0xFF000000
    )

    val defaultTool = getDefaultToolUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        "pen"
    )

    val pressureSensitivity = getPressureSensitivityUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        80
    )

    val palmRejection = getPalmRejectionUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        true
    )

    val onboardingComplete = getOnboardingCompleteUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    val autoSaveInterval = getAutoSaveIntervalUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        2000L
    )

    fun setThemeMode(mode: Int) {
        viewModelScope.launch {
            setThemeModeUseCase(mode)
        }
    }

    fun setGridView(grid: Int) {
        viewModelScope.launch {
            setGridViewUseCase(grid)
        }
    }

    fun setSortOrder(order: Int) {
        viewModelScope.launch {
            setSortOrderUseCase(order)
        }
    }

    fun setShowThumbnails(show: Boolean) {
        viewModelScope.launch {
            setShowThumbnailsUseCase(show)
        }
    }

    fun setDefaultStrokeWidth(width: Float) {
        viewModelScope.launch {
            setDefaultStrokeWidthUseCase(width)
        }
    }

    fun setDefaultStrokeColor(color: Int) {
        viewModelScope.launch {
            setDefaultStrokeColorUseCase(color)
        }
    }

    fun setDefaultTool(tool: String) {
        viewModelScope.launch {
            setDefaultToolUseCase(tool)
        }
    }

    fun setPressureSensitivity(sensitivity: Int) {
        viewModelScope.launch {
            setPressureSensitivityUseCase(sensitivity)
        }
    }

    fun setPalmRejection(enabled: Boolean) {
        viewModelScope.launch {
            setPalmRejectionUseCase(enabled)
        }
    }

    fun setOnboardingComplete(complete: Boolean) {
        viewModelScope.launch {
            setOnboardingCompleteUseCase(complete)
        }
    }

    fun setAutoSaveInterval(interval: Long) {
        viewModelScope.launch {
            setAutoSaveIntervalUseCase(interval)
        }
    }
}