package com.simposio.practica17.data

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class TaskRepository {

    suspend fun getInitialTasks(simulateError: Boolean = false): List<Task> {
        delay(1500L.milliseconds)

        if (simulateError) {
            throw Exception("No se pudo conectar con el servidor de tareas.")
        }

        return listOf(
            Task(id = 1, title = "Estudiar arquitectura MVVM", completed = true),
            Task(id = 2, title = "Implementar StateFlow en Compose", completed = false),
            Task(id = 3, title = "Probar rotación de pantalla", completed = false)
        )
    }
}