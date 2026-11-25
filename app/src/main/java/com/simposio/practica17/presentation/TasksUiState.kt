package com.simposio.practica17.presentation

import com.simposio.practica17.data.Task

enum class TaskFilter(val tag: String) {
    ALL("All"),
    PENDING("Pending"),
    COMPLETED("Completed")
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
    val filteredTasks: List<Task>
        get() = when (activeFilter) {
            TaskFilter.ALL -> tasks
            TaskFilter.PENDING -> tasks.filter { !it.completed }
            TaskFilter.COMPLETED -> tasks.filter { it.completed }
        }
}