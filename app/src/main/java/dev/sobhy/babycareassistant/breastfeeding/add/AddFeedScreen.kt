package dev.sobhy.babycareassistant.breastfeeding.add

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.ui.composable.AddScreens
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.CustomTimePicker
import dev.sobhy.babycareassistant.ui.composable.Loader
import java.time.LocalDate
import java.time.LocalTime
import java.time.Year
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFeedScreen(
    navController: NavController,
    feedingId: String? = null,
    viewModel: AddFeedingViewModel = hiltViewModel(),
) {
    val state by viewModel.addFeedingState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var indexOfTimeTextField by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(Unit) {
        if (feedingId != null) {
            viewModel.getFeedingById(feedingId)
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

    AddScreens(title = "Breastfeeding Data", back = { navController.navigateUp() }) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = if (state.feedingDate.isEqual(LocalDate.of(1000, 1, 1))) {
                        ""
                    } else {
                        state.feedingDate.toString()
                    },
                    onValueChange = {},
                    modifier = Modifier.weight(3f),
                    label = {
                        Text(text = "Breastfeeding Date", fontSize = 10.sp)
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
                Spacer(modifier = Modifier.weight(0.1f))
                OutlinedTextField(
                    value = state.feedingDay,
                    onValueChange = {},
                    label = {
                        Text(text = "Day")
                    },
                    placeholder = {
                        Text(text = "Day")
                    },
                    singleLine = true,
                    modifier = Modifier.weight(2f),
                    supportingText = {},
                    enabled = false
                )
            }
        }
        item {
            CustomTextField(
                value = state.numOfFeedingPerDay,
                onValueChange = { text ->
                    // Only allow digits and ensure number is less than 9
                    if (text.all { it.isDigit() } && (text.toIntOrNull() ?: 0) <= 9) {
                        viewModel.onEvent(
                            AddFeedingUiEvent.NumberOfFeedingsChanged(text)
                        )
                    }
                },
                label = "Number of feedings/day",
                placeHolder = "Ex: 3",
                keyboardType = KeyboardType.Number,
                errorText = state.numberOfFeedingPerDayError
            )
        }
        item {
            if (state.numOfFeedingPerDay.isNotBlank()) {
                if (state.numOfFeedingPerDay.toInt() > 0) {
                    state.feedingTimes.forEachIndexed { index, value ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Time ${index + 1}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp),
                                textAlign = TextAlign.Center
                            )
                            OutlinedTextField(
                                value = if (value.feedingTime == LocalTime.of(0, 0, 30)) {
                                    ""
                                } else {
                                    value.feedingTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
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
                                    .padding(vertical = 2.dp),
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

                            OutlinedTextField(
                                value = value.amountOfMilk,
                                onValueChange = { text ->
                                    viewModel.onEvent(
                                        AddFeedingUiEvent.UpdateAmountFieldsValues(
                                            index,
                                            text.filter { symbol ->
                                                symbol.isDigit()
                                            }
                                        )
                                    )
                                },
                                label = {
                                    Text(text = "Amount of milk")
                                },
                                placeholder = {
                                    Text(text = "EX: 50mm")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                )
                            )
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = {
                    viewModel.onEvent(AddFeedingUiEvent.AddFeedingClicked(feedingId))
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
        yearRange = IntRange(Year.now().value, Year.now().value + 1),
        dateChange = {
            viewModel.onEvent(AddFeedingUiEvent.FeedingDateChanged(it))
            val day = it.dayOfWeek.name
            viewModel.onEvent(AddFeedingUiEvent.FeedingDayChanged(day))
        }
    )
    CustomTimePicker(
        showTimePicker = showTimePicker,
        timeSelected = {
            viewModel.onEvent(
                AddFeedingUiEvent.UpdateTimeFieldsValues(indexOfTimeTextField, it)
            )
            Log.d("time in screen", "$indexOfTimeTextField : $it")
        },
        dismissDialog = { showTimePicker = false }
    )
}