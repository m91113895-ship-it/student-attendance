package com.example.attendance.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: Long,
    val firstName: String,
    val lastName: String,
    val group: String,
    val isPresent: Boolean = false
)
