package com.example.attendance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.attendance.data.local.StudentEntity
import com.example.attendance.ui.viewmodel.AttendanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(viewModel: AttendanceViewModel) {
    val students by viewModel.students.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val message by viewModel.message.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Учёт посещаемости") },
                actions = {
                    IconButton(onClick = { viewModel.syncData() }, enabled = !isSyncing) {
                        Icon(Icons.Default.Refresh, contentDescription = "Синхронизировать")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Блок статистики
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Всего: ${stats.total}", style = MaterialTheme.typography.bodyLarge)
                    Text("Присутствуют: ${stats.present}", color = MaterialTheme.colorScheme.primary)
                    Text("Отсутствуют: ${stats.absent}", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { stats.percent / 100f },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${stats.percent}%", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isSyncing) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (students.isEmpty() && !isSyncing) {
                Text(
                    "Список пуст. Нажмите кнопку обновления в шапке.",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(students) { student ->
                        StudentCard(student) { viewModel.toggleAttendance(student.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentCard(student: StudentEntity, onToggle: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "${student.lastName} ${student.firstName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Группа: ${student.group}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = student.isPresent, onCheckedChange = { onToggle() })
        }
    }
}
