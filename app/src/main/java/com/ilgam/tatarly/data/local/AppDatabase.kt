package com.ilgam.tatarly.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ilgam.tatarly.data.local.dao.WordDao
import com.ilgam.tatarly.data.local.entity.WordEntity

@Database(
    entities = [WordEntity::class],  // все Entity
    version = 1,                     // версия БД
    exportSchema = false             // для разработки false, для продакшена true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao  // метод для доступа к DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tatarly_database"  // имя файла БД
                )
                    .fallbackToDestructiveMigration(false)  // удаляет БД при обновлении версии (только для разработки!)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}