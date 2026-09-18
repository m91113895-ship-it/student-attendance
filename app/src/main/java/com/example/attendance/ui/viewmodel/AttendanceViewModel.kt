package com.example.attendance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.attendance.data.AttendanceRepository
import com.example.attendance.data.AttendanceStats
import com.example.attendance.data.local.StudentEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AttendanceViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {

    val students: StateFlow<List<StudentEntity>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats: StateFlow<AttendanceStats> = students
        .map { repository.getStats(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AttendanceStats(0, 0, 0, 0))

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun syncData() {
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                repository.syncFromServer()
                _message.value = "Синхронизация завершена"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun toggleAttendance(id: Long) {
        viewModelScope.launch { repository.toggleAttendance(id) }
    }

    fun clearMessage() { _message.value = null }

    class Factory(private val repo: AttendanceRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AttendanceViewModel(repo) as T
    }
}
