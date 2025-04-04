package com.example.wordapp.domain

import com.example.wordapp.data.model.Word
import com.example.wordapp.data.reposity.WordRepository
import kotlinx.coroutines.flow.Flow

class GetAllWordsUseCase(private val repository: WordRepository) {
    operator fun invoke(): Flow<List<Word>> = repository.getAllWords()
}
