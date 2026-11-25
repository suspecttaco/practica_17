package com.simposio.practica17.data

data class Task(
    val id: Int,
    val title: String,
    val completed: Boolean = false
)