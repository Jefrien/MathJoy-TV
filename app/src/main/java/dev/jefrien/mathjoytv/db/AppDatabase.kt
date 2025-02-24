package dev.jefrien.mathjoytv.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jefrien.mathjoytv.db.dao.ExamDao
import dev.jefrien.mathjoytv.db.entities.ExamEntity

@Database(entities = [ExamEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun examDao(): ExamDao
}