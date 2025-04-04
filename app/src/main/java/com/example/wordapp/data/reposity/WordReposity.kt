package com.example.wordapp.data.reposity

import com.example.wordapp.data.model.Word
import com.example.wordapp.data.model.WordDao
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    fun getAllWords(): Flow<List<Word>> = wordDao.getAllWords()

    suspend fun getWordById(id: Int): Word? = wordDao.getWordById(id)

    suspend fun insertWord(word: Word) = wordDao.insertWord(word)

    suspend fun updateWord(word: Word) = wordDao.updateWord(word)

    suspend fun deleteWord(word: Word) = wordDao.deleteWord(word)
}
