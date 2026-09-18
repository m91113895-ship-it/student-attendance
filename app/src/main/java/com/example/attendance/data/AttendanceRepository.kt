package com.example.attendance.data

import com.example.attendance.data.local.StudentDao
import com.example.attendance.data.local.StudentEntity
import com.example.attendance.data.remote.AttendanceApi
import kotlinx.coroutines.flow.Flow

class AttendanceRepository(
    private val studentDao: StudentDao,
    private val api: AttendanceApi
) {
    val allStudents: Flow<List<StudentEntity>> = studentDao.getAllStudents()

    suspend fun syncFromServer() {
        try {
            val remote = api.getStudents()
            val entities = remote.map { dto ->
                StudentEntity(
                    id = dto.id,
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    group = dto.group
                )
            }
            studentDao.insertAll(entities)
        } catch (e: Exception) {
            println("Sync error: ${e.message}")
        }
    }

    suspend fun toggleAttendance(id: Long) {
        val student = studentDao.getById(id) ?: return
        studentDao.updateStudent(student.copy(isPresent = !student.isPresent))
    }

    fun getStats(students: List<StudentEntity>): AttendanceStats {
        val total = students.size
        val present = students.count { it.isPresent }
        val absent = total - present
        val percent = if (total > 0) (present * 100 / total) else 0
        return AttendanceStats(total, present, absent, percent)
    }
}

data class AttendanceStats(
    val total: Int,
    val present: Int,
    val absent: Int,
    val percent: Int
)
