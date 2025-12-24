package com.ilgam.tatarly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")  // имя таблицы в БД
data class WordEntity(
    @PrimaryKey(autoGenerate = true)  // автоинкремент ID
    val id: Long = 0L,
    val tatWord: String,         // слово на татарском
    val rusWord: String,         // перевод на русский
    val category: String = "Основные",  // категория слова
    val isLearned: Boolean = false,     // изучено ли
    val difficulty: Int = 1      // сложность (1-легко, 2-средне, 3-сложно)
)