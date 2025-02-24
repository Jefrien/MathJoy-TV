package dev.jefrien.mathjoytv.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class ExamEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "student_name") val studentName: String?,
    @ColumnInfo(name = "responses") val responses: String?
)