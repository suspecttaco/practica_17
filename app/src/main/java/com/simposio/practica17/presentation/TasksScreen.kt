package com.simposio.practica17.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simposio.practica17.presentation.components.LoadStatusContent
import com.simposio.practica17.presentation.components.TasksList

@Composable
fun TasksScreen(
    viewModel: TasksViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TasksContent(
        uiState = uiState,
        onTextChange = viewModel::onTextChange,
        onAddTask = viewModel::addTask,
        onCompletedChange = viewModel::toggleCompleted,
        onDeleteTask = viewModel::deleteTask,
        onFilterChange = viewModel::changeFilter,
        onClearCompleted = viewModel::clearCompleted,
        onReload = { viewModel.loadTasks(simulateError = false) },
        onSimulateError = { viewModel.loadTasks(simulateError = true) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksContent(
    uiState: TasksUiState,
    onTextChange: (String) -> Unit,
    onAddTask: () -> Unit,
    onCompletedChange: (Int) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onFilterChange: (TaskFilter) -> Unit,
    onClearCompleted: () -> Unit,
    onReload: () -> Unit,
    onSimulateError: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MVVM - Gestor de Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = onSimulateError) {
                        Icon(Icons.Default.Warning, contentDescription = "Simular error de carga")
                    }
                    IconButton(onClick = onReload) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar tareas")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LoadStatusContent(
                status = uiState.loadStatus,
                onReload = onReload,
                modifier = Modifier.fillMaxSize()
            ) {
                TasksList(
                    uiState = uiState,
                    onTextChange = onTextChange,
                    onAddTask = onAddTask,
                    onCompletedChange = onCompletedChange,
                    onDeleteTask = onDeleteTask,
                    onFilterChange = onFilterChange,
                    onClearCompleted = onClearCompleted
                )
            }
        }
    }
}
