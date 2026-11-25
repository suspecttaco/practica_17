package com.simposio.practica17.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simposio.practica17.presentation.TaskFilter
import com.simposio.practica17.presentation.TasksUiState

@Composable
fun TasksList(
    uiState: TasksUiState,
    onTextChange: (String) -> Unit,
    onAddTask: () -> Unit,
    onCompletedChange: (Int) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onFilterChange: (TaskFilter) -> Unit,
    onClearCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatsCard(
            total = uiState.totalCount,
            pending = uiState.pendingCount,
            completed = uiState.completedCount
        )
        NewTaskInput(
            text = uiState.newTaskText,
            onTextChange = onTextChange,
            onAddTask = onAddTask
        )
        FilterRow(
            activeFilter = uiState.activeFilter,
            onFilterChange = onFilterChange
        )
        OutlinedButton(
            onClick = onClearCompleted,
            enabled = uiState.completedCount > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar completadas")
        }

        val tasks = uiState.filteredTasks
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay tareas en esta categoría.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { onCompletedChange(task.id) },
                        onDelete = { onDeleteTask(task.id) }
                    )
                }
            }
        }
    }
}
