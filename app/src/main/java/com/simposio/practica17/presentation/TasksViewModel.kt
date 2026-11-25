package com.simposio.practica17.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simposio.practica17.data.Task
import com.simposio.practica17.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private var nextId = 4

    init {
        loadTasks()
    }

    fun loadTasks(simulateError: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadStatus = LoadStatus.Loading) }

            try {
                val obtainedList = repository.getInitialTasks(simulateError)
                nextId = (obtainedList.maxOfOrNull { it.id } ?: 0) + 1

                _uiState.update {
                    it.copy(
                        loadStatus = LoadStatus.Success,
                        tasks = obtainedList
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadStatus = LoadStatus.Error(
                            e.message ?: "Error desconocido al cargar los datos."
                        )
                    )
                }
            }
        }
    }

    fun onTextChanged(newText: String) {
        _uiState.update { it.copy(newTaskText = newText) }
    }

    fun addTask() {
        val cleanTitle = _uiState.value.newTaskText.trim()
        if (cleanTitle.isEmpty()) return

        val newTask = Task(id = nextId++, title = cleanTitle)

        _uiState.update { actualStatus ->
            actualStatus.copy(
                tasks = listOf(newTask) + actualStatus.tasks,
                newTaskText = ""
            )
        }
    }

    fun alternateComplete(taskId: Int) {
        _uiState.update { actualStatus ->
            val updatedList = actualStatus.tasks.map { task ->
                if (task.id == taskId) {
                    task.copy(completed = !task.completed)
                } else {
                    task
                }
            }

            actualStatus.copy(tasks = updatedList)
        }
    }

    fun deleteTask(taskId: Int) {
        _uiState.update { actualStatus ->
            actualStatus.copy(
                tasks = actualStatus.tasks.filterNot { it.id == taskId }
            )
        }
    }

    fun changeFilter(newFilter: TaskFilter) {
        _uiState.update { it.copy(activeFilter = newFilter) }
    }
}