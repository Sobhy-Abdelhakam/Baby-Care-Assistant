package dev.sobhy.babycareassistant.growth.add

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.ui.composable.AddScreens
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.Loader
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGrowthScreen(
    navController: NavController,
    growthId: String? = null,
    viewModel: AddGrowthViewModel = hiltViewModel(),
) {
    val state by viewModel.addGrowthState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (growthId != null) {
            viewModel.getGrowthById(growthId)
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
    AddScreens(title = "Baby Growth Data", back = { navController.popBackStack() }) {
        item {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = "${state.babyAge} months",
                    onValueChange = { },
                    label = { Text("Baby Age") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    (1..12).forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = "$selectionOption months")
                            },
                            onClick = {
                                viewModel.onEvent(AddGrowthUiEvent.BabyAgeChange(selectionOption))
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = if (state.dateOfMeasurement.isEqual(LocalDate.of(1000, 1, 1))) {
                    ""
                } else {
                    state.dateOfMeasurement.toString()
                },
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Date of measurement")
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = state.babyWeight,
                    onValueChange = {text ->
                        viewModel.onEvent(
                            AddGrowthUiEvent.BabyWeightChange(
                                text.filter { it.isDigit() || it == '.' }
                            )
                        )
                    },
                    label = "Baby weight",
                    placeHolder = "Ex: 1 KG",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    keyboardType = KeyboardType.Decimal,
                    errorText = state.weightError
                )
                CustomTextField(
                    value = state.babyHeight,
                    onValueChange = {text ->
                        viewModel.onEvent(
                            AddGrowthUiEvent.BabyHeightChange(
                                text.filter { it.isDigit() || it == '.' }
                            )
                        )
                    },
                    label = "Baby height",
                    placeHolder = "Ex: 1 CM",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    keyboardType = KeyboardType.Decimal,
                    errorText = state.heightError
                )
            }
        }
        item {
            CustomTextField(
                value = state.headCircumference,
                onValueChange = { text ->
                    viewModel.onEvent(
                        AddGrowthUiEvent.HeadCircumferenceChange(
                            text.filter { it.isDigit() || it == '.' }
                        )
                    )
                },
                label = "Head circumference",
                placeHolder = "Ex: 20 CM",
                keyboardType = KeyboardType.Decimal,
                errorText = state.headCircumferenceError
            )
        }
        item {
            Button(
                onClick = {
                    viewModel.onEvent(AddGrowthUiEvent.AddGrowth(growthId))
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
    val today = LocalDate.now()
    val todayMillis = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    CustomDatePicker(
        showDatePicker = showDatePicker,
        dismissDatePicker = {
            showDatePicker = false
        },
        yearRange = IntRange(Year.now().value - 2, Year.now().value),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= todayMillis
            }
        },
        dateChange = {
            viewModel.onEvent(AddGrowthUiEvent.DataOfMeasurementChange(it))
        }
    )
}