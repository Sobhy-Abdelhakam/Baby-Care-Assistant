package dev.sobhy.babycareassistant.sleep.add

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.ui.composable.AddScreens
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.CustomTimePicker
import dev.sobhy.babycareassistant.ui.composable.Loader
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSleepScreen(
    navController: NavController,
    sleepId: String? = null,
    viewModel: AddSleepViewModel = hiltViewModel(),
) {
    val state by viewModel.addSleepState.collectAsState()
    val babyAge = listOf(
        "0 - 3 months",
        "4 - 11 months",
        "1 - 2 years",
        "3 - 5 years",
    )
    var ageDropDownExpanded by remember { mutableStateOf(false) }
    val sleepQuality = listOf(
        "Good",
        "average",
        "bad",
    )
    var sleepQualityExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var indexOfTimeTextField by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        if (sleepId != null) {
            viewModel.getSleepById(sleepId)
        }
    }
    if (state.loading) {
        Loader()
    }
    if (state.success) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    AddScreens(title = "Baby Sleep Data", back = { navController.popBackStack() }) {
        item {
            OutlinedTextField(
                value = if (state.date.isEqual(LocalDate.of(1000, 1, 1))) {
                    ""
                } else {
                    state.date.toString()
                },
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Date of sleep")
                },
                singleLine = true,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                isError = state.dateError != null,
                supportingText = {
                    if (state.dateError != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.dateError!!,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            )
        }
        item {
            ExposedDropdownMenuBox(
                expanded = ageDropDownExpanded,
                onExpandedChange = {
                    ageDropDownExpanded = !ageDropDownExpanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = state.babyAge,
                    onValueChange = { },
                    label = { Text("Baby Age") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = ageDropDownExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                )
                ExposedDropdownMenu(
                    expanded = ageDropDownExpanded,
                    onDismissRequest = {
                        ageDropDownExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    babyAge.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = selectionOption)
                            },
                            onClick = {
                                viewModel.onEvent(AddSleepUiEvent.BabyAgeChange(selectionOption))
                                ageDropDownExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        item {
            ExposedDropdownMenuBox(
                expanded = sleepQualityExpanded,
                onExpandedChange = {
                    sleepQualityExpanded = !sleepQualityExpanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = state.sleepQuality,
                    onValueChange = { },
                    label = { Text("Sleep Quality") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = sleepQualityExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                )
                ExposedDropdownMenu(
                    expanded = sleepQualityExpanded,
                    onDismissRequest = {
                        sleepQualityExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    sleepQuality.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = selectionOption)
                            },
                            onClick = {
                                viewModel.onEvent(AddSleepUiEvent.SleepQualityChange(selectionOption))
                                sleepQualityExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        item {
            Text(
                text = "Add sleep time",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        item {
            OutlinedTextField(
                value = if (state.sleepTime == LocalTime.of(0, 0, 30)) {
                    ""
                } else {
                    state.sleepTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                },
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                label = {
                    Text(text = "Sleep time")
                },
                placeholder = {
                    Text(text = "Sleep time")
                },
                trailingIcon = {
                    ClickableText(
                        text = AnnotatedString("Set time"),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        showTimePicker = true
                        indexOfTimeTextField = 1
                    }
                }
            )
        }
        item {
            OutlinedTextField(
                value = if (state.wakeUpTime == LocalTime.of(0, 0, 30)) {
                    ""
                } else {
                    state.wakeUpTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                },
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                label = {
                    Text(text = "Wake up time")
                },
                placeholder = {
                    Text(text = "Wake up time")
                },
                trailingIcon = {
                    ClickableText(
                        text = AnnotatedString("Set time"),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        showTimePicker = true
                        indexOfTimeTextField = 2
                    }
                }
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomTextField(
                    value = "${state.duration.toDuration(DurationUnit.MINUTES)}",
                    onValueChange = {},
                    label = "Sleep duration",
                    placeHolder = "00:00",
                    readOnly = true,
                    modifier = Modifier
                        .weight(5f)
                        .padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    onClick = {
                        viewModel.onEvent(AddSleepUiEvent.AddSleepTime)
                    },
                    modifier = Modifier
                        .weight(0.9f)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(4.dp),
                    enabled = state.sleepTime != LocalTime.of(0, 0, 30) && state.wakeUpTime != LocalTime.of(0,0,30)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Icon",
                        modifier = Modifier
                            .fillParentMaxSize(0.1f)
                    )
                }
            }
        }
        item {
            if (state.sleepTimesList.isNotEmpty()) {
                state.sleepTimesList.forEachIndexed { index, sleepTime ->
                    val fromFormat = LocalTime.parse(sleepTime.from)
                        .format(DateTimeFormatter.ofPattern("hh:mm a"))
                    val toFormat =
                        LocalTime.parse(sleepTime.to).format(DateTimeFormatter.ofPattern("hh:mm a"))
                    Card(
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.outlinedCardColors(),
                        border = BorderStroke(1.dp, color = Color.Gray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Sleep time ${index + 1}")
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(AddSleepUiEvent.OnDeleteSleepTimeClicked(index))
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteForever,
                                    contentDescription = "Edit Icon",
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                                .padding(horizontal = 8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Time", color = Color.Gray)
                            Text(
                                text = "$fromFormat : $toFormat"
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                                .padding(horizontal = 8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Sleep duration", color = Color.Gray)
                            Text(text = "${sleepTime.duration} hours")
                        }
                    }
                }
            }
        }
        item {
            if (state.totalSleepTime != 0.0){
                Text(text = "Total sleep for day: ${state.totalSleepTime} Hours")
            }
        }
        item {
            state.sleepTimesError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        item {
            Button(
                onClick = {
                    viewModel.onEvent(AddSleepUiEvent.SaveSleepData(sleepId))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = "Save Data",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        item {
            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
    CustomDatePicker(
        showDatePicker = showDatePicker,
        dismissDatePicker = {
            showDatePicker = false
        },
        dateChange = {
            viewModel.onEvent(AddSleepUiEvent.DateOfSleepChanged(it))
        }
    )
    CustomTimePicker(
        showTimePicker = showTimePicker,
        timeSelected = { selectedTime ->
            if (indexOfTimeTextField == 1) {
                viewModel.onEvent(
                    AddSleepUiEvent.SleepTimeChange(selectedTime)
                )
                val duration = Duration.between(
                    selectedTime,
                    state.wakeUpTime
                )
                val temp = if (duration.isNegative) {
                    duration.plusDays(1)
                } else {
                    duration
                }
                Log.d("duration", temp.toString())
                viewModel.onEvent(
                    AddSleepUiEvent.DurationChange(
                        temp.toMinutes()
                    )
                )
            } else if (indexOfTimeTextField == 2) {
                viewModel.onEvent(
                    AddSleepUiEvent.WakeUpTimeChange(selectedTime)
                )
                val duration = Duration.between(
                    state.sleepTime,
                    selectedTime
                )
                val temp = if (duration.isNegative) {
                    duration.plusDays(1)
                } else {
                    duration
                }
                Log.d("duration", temp.toString())
                viewModel.onEvent(
                    AddSleepUiEvent.DurationChange(
                        temp.toMinutes()
                    )
                )
            }
        },
        dismissDialog = { showTimePicker = false }
    )
}