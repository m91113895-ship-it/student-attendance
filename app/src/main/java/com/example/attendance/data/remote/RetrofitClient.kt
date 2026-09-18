package com.example.attendance.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Замените на ваш API. Для теста можно использовать любой публичный JSON-API.
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: AttendanceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AttendanceApi::class.java)
    }
}
