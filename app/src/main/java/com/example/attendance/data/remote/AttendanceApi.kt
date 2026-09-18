package com.example.attendance.data.remote

import retrofit2.http.GET

interface AttendanceApi {
    @GET("students")
    suspend fun getStudents(): List<StudentDto>
}
