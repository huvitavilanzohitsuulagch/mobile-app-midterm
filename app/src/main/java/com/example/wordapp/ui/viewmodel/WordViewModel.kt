package com.example.wordapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordapp.data.datastore.DisplayMode
import com.example.wordapp.data.datastore.SettingsDataStore
import com.example.wordapp.data.model.Word
import com.example.wordapp.data.reposity.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MainUiState(
    val words: List<Word> = emptyList(),
    val currentIndex: Int = 0,
    val displayMode: DisplayMode = DisplayMode.BOTH,
    val isWordListEmpty: Boolean = true
)

class WordViewModel(
    private val repository: WordRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        // Үгсийн санг ачаалж, displayMode-г ажиглана
        viewModelScope.launch {
            combine(
                repository.getAllWords(),
                settingsDataStore.displayModeFlow
            ) { words, mode ->
                MainUiState(
                    words = words,
                    displayMode = mode,
                    currentIndex = 0,
                    isWordListEmpty = words.isEmpty()
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun nextWord() {
        val currentState = _uiState.value
        if (currentState.words.isNotEmpty()) {
            val newIndex = (currentState.currentIndex + 1) % currentState.words.size
            _uiState.value = currentState.copy(currentIndex = newIndex)
        }
    }

    fun previousWord() {
        val currentState = _uiState.value
        if (currentState.words.isNotEmpty()) {
            val newIndex =
                if (currentState.currentIndex - 1 < 0) currentState.words.size - 1
                else currentState.currentIndex - 1
            _uiState.value = currentState.copy(currentIndex = newIndex)
        }
    }

    fun deleteCurrentWord() {
        val currentState = _uiState.value
        if (currentState.words.isNotEmpty()) {
            viewModelScope.launch {
                repository.deleteWord(currentState.words[currentState.currentIndex])
            }
        }
    }

    fun setDisplayMode(mode: DisplayMode) {
        viewModelScope.launch {
            settingsDataStore.setDisplayMode(mode)
        }
    }

    suspend fun insertWord(word: Word) {
        repository.insertWord(word)
    }

    suspend fun updateWord(word: Word) {
        repository.updateWord(word)
    }
}
