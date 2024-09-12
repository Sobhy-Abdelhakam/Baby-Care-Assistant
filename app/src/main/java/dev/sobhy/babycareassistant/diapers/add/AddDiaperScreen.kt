package dev.sobhy.babycareassistant.diapers.add

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.breastfeeding.add.AddFeedingUiEvent
import dev.sobhy.babycareassistant.ui.composable.AddScreens
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.CustomTimePicker
import dev.sobhy.babycareassistant.ui.composable.Loader
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddDiaperScreen(
    navController: NavController,
    diaperId: String? = null,
    viewModel: AddDiaperViewModel = hiltViewModel()
) {
    val state by viewModel.addDiaperState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var indexOfTimeTextField by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        if (diaperId != null){
            viewModel.getDiaperById(diaperId)
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
    
    AddScreens(title = "Diapers Data", back = { navController.popBackStack() }) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = if (state.diaperDate.isEqual(LocalDate.of(1000, 1, 1))) {
                        ""
                    } else {
                        state.diaperDate.toString()
                    },
                    onValueChange = {},
                    modifier = Modifier.weight(2.5f),
                    label = {
                        Text(text = "Date")
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
                Spacer(modifier = Modifier.weight(0.2f))
                OutlinedTextField(
                    value = state.diaperDay,
                    onValueChange = {},
                    label = {
                        Text(text = "Day")
                    },
                    placeholder = {
                        Text(text = "Day")
                    },
                    singleLine = true,
                    enabled = false,
                    modifier = Modifier.weight(1.5f),
                    supportingText = {}
                )
            }
        }
        item {
            CustomTextField(
                value = state.numOfDiaperChange,
                onValueChange = { text ->
                    viewModel.onEvent(
                        AddDiaperUiEvent.NumberOfDiaperChanged(
                            text.filter { symbol ->
                                symbol.isDigit()
                            }
                        )
                    )
                },
                label = "Number of diaper changes",
                placeHolder = "Ex: 4",
                keyboardType = KeyboardType.Number
            )
        }
        item {
            if (state.numOfDiaperChange.isNotBlank()) {
                if (state.numOfDiaperChange.toInt() > 0) {
                    state.timesOfDiaperChange.forEachIndexed { index, value ->
                        Text(
                            text = "Time ${index + 1}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = if(value == LocalTime.of(0,0,30)){
                                ""
                            }else{
                                value.format(DateTimeFormatter.ofPattern("hh:mm a"))
                            },
                            onValueChange = { },
                            readOnly = true,
                            label = {
                                Text(text = "Time")
                            },
                            placeholder = {
                                Text(text = "6:30 AM")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
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
                                    indexOfTimeTextField = index
                                }
                            }
                        )
                    }
                }
            }
        }
        item {
            Button(
                onClick = {
                    viewModel.onEvent(AddDiaperUiEvent.AddDiaperClicked(diaperId))
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
            viewModel.onEvent(AddDiaperUiEvent.DiaperDateChanged(it))
            val day = it.dayOfWeek.name
            viewModel.onEvent(AddDiaperUiEvent.DiaperDayChanged(day))
        }
    )
    CustomTimePicker(
        showTimePicker = showTimePicker,
        timeSelected = {
            viewModel.onEvent(
                AddDiaperUiEvent.UpdateTimeFieldsValues(indexOfTimeTextField, it)
            )
        },
        dismissDialog = { showTimePicker = false }
    )

}