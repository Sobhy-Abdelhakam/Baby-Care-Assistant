package dev.sobhy.babycareassistant.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val state by viewModel.notificationState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            when (state) {
                is NotificationState.Error -> {
                    Text(
                        text = "Error: ${(state as NotificationState.Error).message}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                NotificationState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                is NotificationState.Success -> {
                    val notifications = (state as NotificationState.Success).notifications
                    if (notifications.isEmpty()) {
                        Text(
                            text = "No notifications found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    LazyColumn {
                        items(notifications) { notification ->
                            NotificationItem(
                                title = notification.title,
                                description = notification.message,
                                dateAndTime = SimpleDateFormat(
                                    "dd/MM/yyyy hh:mm a",
                                    Locale.getDefault()
                                ).format(Date(notification.date))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    title: String,
    description: String,
    dateAndTime: String,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(1.dp, color = Color.Gray, shape = MaterialTheme.shapes.small)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = title, color = Color.Black)
            Icon(
                imageVector = Icons.Default.NotificationImportant,
                contentDescription = "Notification Icon"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(text = description, color = Color.Black)
            Text(
                text = dateAndTime,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.End
            )
        }

    }
}