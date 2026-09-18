package com.example.attendance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.attendance.data.AttendanceRepository
import com.example.attendance.data.local.AppDatabase
import com.example.attendance.data.remote.RetrofitClient
import com.example.attendance.ui.screens.AttendanceScreen
import com.example.attendance.ui.viewmodel.AttendanceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = AttendanceRepository(database.studentDao(), RetrofitClient.api)
        val viewModel = AttendanceViewModel.Factory(repository)
            .create(AttendanceViewModel::class.java)

        setContent {
            MaterialTheme {
                Surface {
                    AttendanceScreen(viewModel = viewModel)
                }
            }
        }
    }
}
