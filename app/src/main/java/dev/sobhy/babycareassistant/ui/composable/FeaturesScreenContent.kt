package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.sobhy.babycareassistant.ui.classes.FeaturesScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FeaturesScreenContent(
    isLoading: Boolean,
    data: List<T>,
    errorMessage: String?,
    screenTitle: String,
    buttonText: String,
    itemContent: @Composable (T) -> Unit,
    emptyMessage: String,
    showAddButton: Boolean = true,
    addButtonClick: () -> Unit,
    backButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = screenTitle)
                },
                navigationIcon = {
                    IconButton(onClick = backButtonClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                )
            )
        },
        bottomBar = {
            if (showAddButton) {
                BottomAppBar(
                    containerColor = Color.Transparent,
                ) {
                    Button(
                        onClick = addButtonClick,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = buttonText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValue ->
        when {
            isLoading -> {
                Loader()
            }

            errorMessage != null -> {
                ErrorScreen(errorMessage = errorMessage)
            }

            data.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue)
                        .padding(16.dp)
                ) {
                    items(data) { item ->
                        itemContent(item)
                    }
                }
            }

            else -> {
                EmptyScreen(emptyMessage)
            }
        }
    }
}


@Composable
fun ErrorScreen(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
        }
    }
}
@Composable
fun EmptyScreen(emptyMessage: String = "No data available") {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emptyMessage)
    }
}