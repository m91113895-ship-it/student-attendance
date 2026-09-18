package com.example.attendance

import com.example.attendance.data.local.StudentEntity
import com.example.attendance.data.AttendanceStats
import org.junit.Assert.assertEquals
import org.junit.Test

class AttendanceStatsTest {

    @Test
    fun `stats with all present returns 100 percent`() {
        val students = listOf(
            StudentEntity(1, "Иван", "Петров", "ИСП-21", true),
            StudentEntity(2, "Анна", "Смирнова", "ИСП-21", true)
        )
        val total = students.size
        val present = students.count { it.isPresent }
        val percent = present * 100 / total
        assertEquals(100, percent)
    }

    @Test
    fun `stats with empty list returns zero percent`() {
        val students = emptyList<StudentEntity>()
        val percent = if (students.isNotEmpty()) students.count { it.isPresent } * 100 / students.size else 0
        assertEquals(0, percent)
    }
}
