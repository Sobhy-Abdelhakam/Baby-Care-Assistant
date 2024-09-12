package dev.sobhy.babycareassistant.vaccination.addvaccination

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.ui.composable.AddScreens
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.Loader
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccinationScreen(
    navController: NavController,
    viewModel: AddVaccinationViewModel = hiltViewModel(),
) {
    val state by viewModel.addVaccinationState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    val nameChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddVaccinationEvent.VaccinationNameChange(it))
        }
    }
    val dateChange = remember<(LocalDate) -> Unit> {
        {
            viewModel.onEvent(AddVaccinationEvent.VaccinationDateChange(it))
        }
    }
    val dayChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddVaccinationEvent.VaccinationDayChange(it))
        }
    }
    val codeChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddVaccinationEvent.VaccinationCodeChange(it))
        }
    }
    val ageGroupChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddVaccinationEvent.AgeGroupChange(it))
        }
    }
    val reasonChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddVaccinationEvent.VaccinationReasonChange(it))
        }
    }
    val notesChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddVaccinationEvent.OtherNotesChange(it))
        }
    }

    if (state.loading) {
        Loader()
    }
    if (state.success) {
        LaunchedEffect(Unit) {
            navController.navigateUp()
        }
    }


    val items = listOf(
        "0 - 3 months",
        "3 - 6 months",
        "6 - 9 months",
        "9 - 12 months",
        "12 - 18 months"
    )
    var expanded by remember { mutableStateOf(false) }
    AddScreens(title = "Add Vaccination", back = { navController.navigateUp() }) {
        item {
            CustomTextField(
                value = state.vaccinationName,
                onValueChange = nameChange,
                label = "Vaccination Name",
                placeHolder = "Vaccination Name",
                errorText = state.nameError
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = if (state.vaccinationDate.isEqual(LocalDate.of(1000, 1, 1))) {
                        ""
                    } else {
                        state.vaccinationDate.toString()
                    },
                    onValueChange = {},
                    modifier = Modifier.weight(2.5f),
                    label = {
                        Text(text = "Vaccination Date")
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
                    value = state.vaccinationDay,
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
                value = state.vaccinationCode,
                onValueChange = codeChange,
                label = "Vaccination Code",
                placeHolder = "Vaccination Code",
                errorText = state.codeError
            )
        }

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
                    value = state.ageGroup,
                    onValueChange = { },
                    label = { Text("Age Group") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    isError = state.ageGroupError != null,
                    supportingText = {
                        if (state.ageGroupError != null) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = state.ageGroupError!!,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = selectionOption)
                            },
                            onClick = {
                                ageGroupChange(selectionOption)
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        item {
            CustomTextField(
                value = state.vaccinationReason,
                onValueChange = reasonChange,
                label = "Reason for vaccination",
                placeHolder = "Reason for vaccination",
                errorText = state.reasonError,
                singleLine = false
            )
        }
        item {
            CustomTextField(
                value = state.otherNotes,
                onValueChange = notesChange,
                label = "Other notes",
                placeHolder = "Other notes",
                singleLine = false
            )
        }
        item {
            Button(
                onClick = {
                    viewModel.onEvent(AddVaccinationEvent.Submit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = "Add Vaccination",
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
            dateChange(it)
            val day = it.dayOfWeek.name
            dayChange(day)
        }
    )
}