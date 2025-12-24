package com.ilgam.tatarly.data.repository

import com.ilgam.tatarly.data.local.dao.WordDao
import com.ilgam.tatarly.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

class WordRepository constructor(
    private val wordDao: WordDao
) {
    // Получение данных (Flow автоматически обновляет UI)
    val allWords: Flow<List<WordEntity>> = wordDao.getAllWords()

    fun getUnlearnedWords(): Flow<List<WordEntity>> = wordDao.getUnlearnedWords()

    fun getWordsByCategory(category: String): Flow<List<WordEntity>> =
        wordDao.getWordsByCategory(category)

    // Операции с БД (suspend - должны вызываться из корутины)
    suspend fun insertWord(word: WordEntity): Long = wordDao.insertWord(word)

    suspend fun updateWord(word: WordEntity) = wordDao.updateWord(word)

    suspend fun deleteWord(word: WordEntity) = wordDao.deleteWord(word)

    suspend fun markAsLearned(wordId: Long, learned: Boolean = true) =
        wordDao.updateLearnedStatus(wordId, learned)

    // Начальное наполнение БД
    suspend fun populateInitialData() {
        val initialWords = listOf(
            WordEntity(tatWord = "сәлам", rusWord = "привет", category = "Приветствия"),
            WordEntity(tatWord = "сау булыгыз", rusWord = "до свидания", category = "Приветствия"),
            WordEntity(tatWord = "рәхмәт", rusWord = "спасибо", category = "Вежливость"),
            WordEntity(tatWord = "зинһар", rusWord = "пожалуйста", category = "Вежливость"),
            WordEntity(tatWord = "әйе", rusWord = "да", category = "Основные"),
            WordEntity(tatWord = "юк", rusWord = "нет", category = "Основные"),
            WordEntity(tatWord = "мин", rusWord = "я", category = "Местоимения"),
            WordEntity(tatWord = "син", rusWord = "ты", category = "Местоимения"),
            WordEntity(tatWord = "ул", rusWord = "он/она", category = "Местоимения"),
            WordEntity(tatWord = "без", rusWord = "мы", category = "Местоимения")
        )
        wordDao.insertAll(initialWords)
    }
}