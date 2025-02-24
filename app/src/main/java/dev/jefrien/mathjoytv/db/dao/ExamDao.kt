package dev.jefrien.mathjoytv.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.jefrien.mathjoytv.db.entities.ExamEntity

@Dao
interface ExamDao {
    @Query("SELECT * FROM exams")
    fun getAll(): List<ExamEntity>

    @Query("SELECT * FROM exams WHERE student_name = :studentName")
    fun loadAllByStudentName(studentName: String): List<ExamEntity>

    @Query("SELECT * FROM exams LIMIT 3")
    fun loadLastThree(): List<ExamEntity>

    @Insert
    fun insertAll(vararg examEntities: ExamEntity)

    @Delete
    fun deleteAll(examEntity: ExamEntity)

}