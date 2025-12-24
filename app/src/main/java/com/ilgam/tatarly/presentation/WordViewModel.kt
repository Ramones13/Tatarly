package com.ilgam.tatarly.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgam.tatarly.data.local.entity.WordEntity
import com.ilgam.tatarly.data.repository.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Пока без Hilt для простоты, потом добавим
class WordViewModel(
    private val repository: WordRepository
) : ViewModel() {

    // Состояние UI
    private val _uiState = MutableStateFlow(WordUiState())
    val uiState: StateFlow<WordUiState> = _uiState.asStateFlow()

    init {
        loadWords()
        viewModelScope.launch {
            // Проверяем, пустая ли БД, и заполняем начальными данными
            if (repository.allWords.first().isEmpty()) {
                repository.populateInitialData()
            }
        }
    }

    private fun loadWords() {
        viewModelScope.launch {
            repository.allWords.collect { words ->
                _uiState.update { it.copy(words = words) }
            }
        }
    }

    fun addWord(tatWord: String, rusWord: String, category: String = "Основные") {
        viewModelScope.launch {
            val newWord = WordEntity(
                tatWord = tatWord,
                rusWord = rusWord,
                category = category
            )
            repository.insertWord(newWord)
        }
    }

    fun markAsLearned(wordId: Long, learned: Boolean) {
        viewModelScope.launch {
            repository.markAsLearned(wordId, learned)
        }
    }

    fun deleteWord(word: WordEntity) {
        viewModelScope.launch {
            repository.deleteWord(word)
        }
    }
}

// Состояние UI
data class WordUiState(
    val words: List<WordEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)