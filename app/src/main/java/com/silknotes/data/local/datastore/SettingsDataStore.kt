package com.silknotes.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SettingsKeys {
    val THEME_MODE = intPreferencesKey("theme_mode") // 0 = system, 1 = light, 2 = dark
    val GRID_VIEW = intPreferencesKey("grid_view") // 0 = list, 1 = grid
    val SORT_ORDER = intPreferencesKey("sort_order") // 0 = date_desc, 1 = date_asc, 2 = title_asc, 3 = title_desc
    val SHOW_THUMBNAILS = intPreferencesKey("show_thumbnails") // 0 = false, 1 = true
    val DEFAULT_STROKE_WIDTH = intPreferencesKey("default_stroke_width") // in px * 100
    val DEFAULT_STROKE_COLOR = intPreferencesKey("default_stroke_color")
    val DEFAULT_TOOL = stringPreferencesKey("default_tool") // "pen", "marker", "eraser"
    val PRESSURE_SENSITIVITY = intPreferencesKey("pressure_sensitivity") // 0-100
    val PALM_REJECTION = intPreferencesKey("palm_rejection") // 0 = false, 1 = true
    val ONBOARDING_COMPLETE = intPreferencesKey("onboarding_complete") // 0 = false, 1 = true
    val AUTO_SAVE_INTERVAL = intPreferencesKey("auto_save_interval") // milliseconds
}

class SettingsDataStore(private val dataStore: DataStore<Preferences>) {
    companion object {
        @Volatile private var INSTANCE: SettingsDataStore? = null

        fun getInstance(context: Context): SettingsDataStore {
            return INSTANCE ?: synchronized(this) {
                val dataStore = context.preferencesDataStore(name = "settings")
                val instance = SettingsDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    val themeMode: Flow<Int> = dataStore.data
        .map { it[SettingsKeys.THEME_MODE] ?: 0 }

    val gridView: Flow<Int> = dataStore.data
        .map { it[SettingsKeys.GRID_VIEW] ?: 1 }

    val sortOrder: Flow<Int> = dataStore.data
        .map { it[SettingsKeys.SORT_ORDER] ?: 0 }

    val showThumbnails: Flow<Boolean> = dataStore.data
        .map { (it[SettingsKeys.SHOW_THUMBNAILS] ?: 1) == 1 }

    val defaultStrokeWidth: Flow<Float> = dataStore.data
        .map { ((it[SettingsKeys.DEFAULT_STROKE_WIDTH] ?: 300) / 100f) }

    val defaultStrokeColor: Flow<Int> = dataStore.data
        .map { it[SettingsKeys.DEFAULT_STROKE_COLOR] ?: 0xFF000000.toInt() }

    val defaultTool: Flow<String> = dataStore.data
        .map { it[SettingsKeys.DEFAULT_TOOL] ?: "pen" }

    val pressureSensitivity: Flow<Int> = dataStore.data
        .map { it[SettingsKeys.PRESSURE_SENSITIVITY] ?: 80 }

    val palmRejection: Flow<Boolean> = dataStore.data
        .map { (it[SettingsKeys.PALM_REJECTION] ?: 1) == 1 }

    val onboardingComplete: Flow<Boolean> = dataStore.data
        .map { (it[SettingsKeys.ONBOARDING_COMPLETE] ?: 0) == 1 }

    val autoSaveInterval: Flow<Long> = dataStore.data
        .map { (it[SettingsKeys.AUTO_SAVE_INTERVAL] ?: 2000).toLong() }

    suspend fun setThemeMode(mode: Int): Boolean {
        dataStore.edit { it[SettingsKeys.THEME_MODE] = mode.coerceIn(0, 2) }
        return true
    }

    suspend fun setGridView(grid: Int): Boolean {
        dataStore.edit { it[SettingsKeys.GRID_VIEW] = grid.coerceIn(0, 1) }
        return true
    }

    suspend fun setSortOrder(order: Int): Boolean {
        dataStore.edit { it[SettingsKeys.SORT_ORDER] = order.coerceIn(0, 3) }
        return true
    }

    suspend fun setShowThumbnails(show: Boolean): Boolean {
        dataStore.edit { it[SettingsKeys.SHOW_THUMBNAILS] = if (show) 1 else 0 }
        return true
    }

    suspend fun setDefaultStrokeWidth(width: Float): Boolean {
        dataStore.edit { it[SettingsKeys.DEFAULT_STROKE_WIDTH] = (width * 100).toInt() }
        return true
    }

    suspend fun setDefaultStrokeColor(color: Int): Boolean {
        dataStore.edit { it[SettingsKeys.DEFAULT_STROKE_COLOR] = color }
        return true
    }

    suspend fun setDefaultTool(tool: String): Boolean {
        dataStore.edit { it[SettingsKeys.DEFAULT_TOOL] = tool }
        return true
    }

    suspend fun setPressureSensitivity(sensitivity: Int): Boolean {
        dataStore.edit { it[SettingsKeys.PRESSURE_SENSITIVITY] = sensitivity.coerceIn(0, 100) }
        return true
    }

    suspend fun setPalmRejection(enabled: Boolean): Boolean {
        dataStore.edit { it[SettingsKeys.PALM_REJECTION] = if (enabled) 1 else 0 }
        return true
    }

    suspend fun setOnboardingComplete(complete: Boolean): Boolean {
        dataStore.edit { it[SettingsKeys.ONBOARDING_COMPLETE] = if (complete) 1 else 0 }
        return true
    }

    suspend fun setAutoSaveInterval(interval: Long): Boolean {
        dataStore.edit { it[SettingsKeys.AUTO_SAVE_INTERVAL] = interval.coerceAtLeast(500).toInt() }
        return true
    }
}