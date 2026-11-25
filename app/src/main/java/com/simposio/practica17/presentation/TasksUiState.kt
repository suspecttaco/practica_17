package com.simposio.practica17.presentation

import com.simposio.practica17.data.Task

enum class TaskFilter(val label: String) {
    ALL("Todas"),
    PENDING("Pendientes"),
    COMPLETED("Completadas")
}

sealed interface LoadStatus {
    data object Loading : LoadStatus
    data object Success : LoadStatus
    data class Error(val message: String) : LoadStatus
}

data class TasksUiState(
    val loadStatus: LoadStatus = LoadStatus.Loading,
    val tasks: List<Task> = emptyList(),
    val activeFilter: TaskFilter = TaskFilter.ALL,
    val newTaskText: String = ""
) {
    val totalCount: Int
        get() = tasks.size

    val pendingCount: Int
        get() = tasks.count { !it.completed }

    val completedCount: Int
        get() = tasks.count { it.completed }

    val filteredTasks: List<Task>
        get() = when (activeFilter) {
            TaskFilter.ALL -> tasks
            TaskFilter.PENDING -> tasks.filter { !it.completed }
            TaskFilter.COMPLETED -> tasks.filter { it.completed }
        }
}