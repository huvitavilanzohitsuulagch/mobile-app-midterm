package com.example.wordapp.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWordById(id: Int): Word?

    @Insert
    suspend fun insertWord(word: Word): Long

    @Update
    suspend fun updateWord(word: Word): Int

    @Delete
    suspend fun deleteWord(word: Word): Int
}
