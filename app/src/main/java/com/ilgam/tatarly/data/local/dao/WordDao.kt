package com.ilgam.tatarly.data.local.dao

import androidx.room.*
import com.ilgam.tatarly.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    // 1. ЧТЕНИЕ
    @Query("SELECT * FROM words ORDER BY id ASC")
    fun getAllWords(): Flow<List<WordEntity>>  // Flow - автоматические обновления

    @Query("SELECT * FROM words WHERE isLearned = 0")
    fun getUnlearnedWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE category = :category")
    fun getWordsByCategory(category: String): Flow<List<WordEntity>>

    // 2. ДОБАВЛЕНИЕ
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // игнорировать конфликты
    suspend fun insertWord(word: WordEntity): Long  // возвращает ID нового слова

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(words: List<WordEntity>)

    // 3. ОБНОВЛЕНИЕ
    @Update
    suspend fun updateWord(word: WordEntity)

    @Query("UPDATE words SET isLearned = :learned WHERE id = :wordId")
    suspend fun updateLearnedStatus(wordId: Long, learned: Boolean)

    // 4. УДАЛЕНИЕ
    @Delete
    suspend fun deleteWord(word: WordEntity)

    @Query("DELETE FROM words")
    suspend fun deleteAll()

    // 5. ПОИСК
    @Query("SELECT * FROM words WHERE tatWord LIKE :query OR rusWord LIKE :query")
    fun searchWords(query: String): Flow<List<WordEntity>>
}