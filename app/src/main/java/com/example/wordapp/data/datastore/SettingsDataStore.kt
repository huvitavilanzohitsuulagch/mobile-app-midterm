package com.example.wordapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val DISPLAY_MODE_KEY = stringPreferencesKey("display_mode")
        private const val DEFAULT_MODE = "BOTH"
    }

    // displayMode-г унших
    val displayModeFlow = context.settingsDataStore.data.map { preferences ->
        when (preferences[DISPLAY_MODE_KEY] ?: DEFAULT_MODE) {
            "FOREIGN_ONLY" -> DisplayMode.FOREIGN_ONLY
            "MONGOLIAN_ONLY" -> DisplayMode.MONGOLIAN_ONLY
            else -> DisplayMode.BOTH
        }
    }

    // displayMode-г хадгалах
    suspend fun setDisplayMode(mode: DisplayMode) {
        context.settingsDataStore.edit { preferences ->
            preferences[DISPLAY_MODE_KEY] = mode.name
        }
    }
}
