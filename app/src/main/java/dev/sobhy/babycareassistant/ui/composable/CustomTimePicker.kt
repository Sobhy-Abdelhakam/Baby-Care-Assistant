package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    showTimePicker: Boolean,
    timeSelected: (LocalTime) -> Unit,
    dismissDialog: () -> Unit,
) {
    if (showTimePicker.not()) return
    val timePickerState = rememberTimePickerState()
    Dialog(
        onDismissRequest = dismissDialog,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        ElevatedCard(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.extraLarge
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Set time",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        containerColor = Color.White,
                        clockDialColor = Color.White,
                        timeSelectorSelectedContainerColor = Color.White,
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        periodSelectorUnselectedContainerColor = Color.LightGray,
                        periodSelectorSelectedContentColor = Color.Black,
                        periodSelectorUnselectedContentColor = Color.Black,
                    ),
                    modifier = Modifier.padding(8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End,

                ) {
                    Button(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = dismissDialog,
                        colors = ButtonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                    Button(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = {
                            val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            timeSelected(time)
                            dismissDialog()
                        },
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}