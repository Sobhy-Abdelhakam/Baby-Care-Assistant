package dev.sobhy.babycareassistant.sleep

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.sleep.data.model.SleepTime
import dev.sobhy.babycareassistant.ui.composable.FeaturesScreenContent
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BabySleepScreen(
    navController: NavController,
    viewModel: SleepViewModel = hiltViewModel(),
) {
    val state by viewModel.sleepState.collectAsStateWithLifecycle()
    val sleepResult by viewModel.selectedSleepResult.collectAsStateWithLifecycle()
    var showResultDialog by remember { mutableStateOf(false) }
    FeaturesScreenContent(
        isLoading = state.loading,
        data = state.data,
        errorMessage = state.error,
        screenTitle = "Baby Sleep",
        buttonText = "Add Baby Sleep",
        itemContent = {
            SleepItem(
                sleepDate = it.date,
                sleepQuality = it.sleepQuality,
                sleepTimes = it.sleepTimes,
                editButtonClick = {
                    navController.navigate(ScreenRoutes.AddBabySleep.route + "/${it.id}")
                },
                deleteButtonClick = {
                    viewModel.deleteSleep(it.id)
                },
                resultButtonClick = {
                    viewModel.calculateSleepResult(it)
                    showResultDialog = true
                }
            )
        },
        addButtonClick = {
            navController.navigate(ScreenRoutes.AddBabySleep.route + "/${null}")
        },
        backButtonClick = { navController.popBackStack() },
        emptyMessage = "No Sleep Data Found"
    )
    if (showResultDialog) {
        Dialog(
            onDismissRequest = {  },
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = sleepResult.result,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    if (sleepResult.advice.isNotEmpty()) {
                        Text(text = "Sleep Result")
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(8.dp)
                        ) {
                            sleepResult.advice.forEach {
                                Text(text = "• $it")
                            }
                        }
                    }

                    Button(
                        onClick = { showResultDialog = false },
                        contentPadding = PaddingValues(10.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = "OK", fontWeight = FontWeight.Bold)
                    }
                }
            }

        }
    }
}

@Composable
fun SleepItem(
    sleepDate: String,
    sleepQuality: String,
    sleepTimes: List<SleepTime>,
    editButtonClick: () -> Unit,
    deleteButtonClick: () -> Unit,
    resultButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(1.dp, color = Color.Gray),
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
    ) {
        Text(
            text = "Data: $sleepDate",
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Sleep Quality: $sleepQuality",
            modifier = Modifier.padding(8.dp)
        )
        sleepTimes.forEachIndexed { index, sleepTime ->
            val from =
                LocalTime.parse(sleepTime.from).format(DateTimeFormatter.ofPattern("hh:mm a"))
            val to = LocalTime.parse(sleepTime.to).format(DateTimeFormatter.ofPattern("hh:mm a"))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "sleep time ${index + 1}", color = Color.Gray)
                    Text(text = "$from : $to")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "sleep duration", color = Color.Gray)
                    Text(text = "${sleepTime.duration} hours")
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = deleteButtonClick,
                modifier = Modifier
                    .padding(2.dp)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .size(35.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White,
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = "Edit Icon",
                )
            }
            IconButton(
                onClick = editButtonClick,
                modifier = Modifier
                    .padding(2.dp)
                    .background(
                        color = Color(0xFF0C98FD),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .size(35.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White,
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon",
                )
            }

            Button(
                onClick = resultButtonClick,
                contentPadding = PaddingValues(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.padding(start = 2.dp),
            ) {
                Text(text = "Show results", fontWeight = FontWeight.Bold)
            }
        }
    }
}