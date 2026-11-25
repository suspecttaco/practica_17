package com.simposio.practica17.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simposio.practica17.data.Task

@Composable
fun TasksScreen(
    viewModel: TasksViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TasksContent(
        uiState = uiState,
        onTextChange = viewModel::onTextChanged,
        onTaskAdd = viewModel::addTask,
        onCompleteChange = viewModel::alternateComplete,
        onTaskDelete = viewModel::deleteTask,
        onFilterChange = viewModel::changeFilter,
        onReload = { viewModel.loadTasks(simulateError = false) },
        onErrorSimulate = { viewModel.loadTasks(simulateError = true) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksContent(
    uiState: TasksUiState,
    onTextChange: (String) -> Unit,
    onTaskAdd: () -> Unit,
    onCompleteChange: (Int) -> Unit,
    onTaskDelete: (Int) -> Unit,
    onFilterChange: (TaskFilter) -> Unit,
    onReload: () -> Unit,
    onErrorSimulate: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MVVM - Gestor de Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = onErrorSimulate) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Simular error de carga"
                        )
                    }

                    IconButton(onClick = onReload) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar tareas"
                        )
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
            when (val status = uiState.loadStatus) {
                is LoadStatus.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator()
                        Text("Cargando tareas...", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                is LoadStatus.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = status.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = onReload) {
                            Text("Reintentar")
                        }
                    }
                }
                is LoadStatus.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = uiState.newTaskText,
                                onValueChange = onTextChange,
                                label = { Text("Nueva tarea") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            FilledIconButton(onClick = onTaskAdd) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar tarea"
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TaskFilter.entries.forEach { filter ->
                                FilterChip(
                                    selected = uiState.activeFilter == filter,
                                    onClick = { onFilterChange(filter) },
                                    label = { Text(filter.tag) }
                                )
                            }
                        }
                        if (uiState.filteredTasks.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay tareas en esta categoria.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = uiState.filteredTasks,
                                    key = { it.id }
                                ) { task ->
                                    TaskItem(
                                        task = task,
                                        onAlternate = { onCompleteChange(task.id) },
                                        onDelete = { onTaskDelete(task.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onAlternate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onAlternate() }
            )
            Text(
                text = task.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textDecoration = if (task.completed) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


