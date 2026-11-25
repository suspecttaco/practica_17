package com.simposio.practica17.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simposio.practica17.presentation.LoadStatus

@Composable
fun LoadStatusContent(
    status: LoadStatus,
    onReload: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when (status) {
        LoadStatus.Success -> content()
        LoadStatus.Loading -> CenteredMessage(modifier) {
            CircularProgressIndicator()
            Text("Cargando tareas...", style = MaterialTheme.typography.bodyLarge)
        }
        is LoadStatus.Error -> CenteredMessage(modifier) {
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
            Button(onClick = onReload) { Text("Reintentar") }
        }
    }
}

@Composable
private fun CenteredMessage(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}
