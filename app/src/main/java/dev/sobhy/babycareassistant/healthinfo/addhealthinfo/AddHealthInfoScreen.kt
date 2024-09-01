package dev.sobhy.babycareassistant.healthinfo.addhealthinfo

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.healthinfo.data.model.HealthInfo
import dev.sobhy.babycareassistant.ui.composable.AddScreens
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.Loader
import java.time.LocalDate

@Composable
fun AddHealthInfoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    healthInfoId: String? = null,
    viewModel: AddHealthInfoViewModel = hiltViewModel()
) {
    val state by viewModel.addHealthInfoState.collectAsStateWithLifecycle()
    val dateChange = remember<(LocalDate) -> Unit> {
        {
            viewModel.onEvent(AddHealthInfoEvent.DateChange(it))
        }
    }
    val explanationChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddHealthInfoEvent.ExplanationChange(it))
        }
    }
    val noteChange = remember<(String) -> Unit> {
        {
            viewModel.onEvent(AddHealthInfoEvent.NoteChange(it))
        }
    }
    LaunchedEffect(Unit) {
        if (healthInfoId != null){
            viewModel.getHealthInfoById(healthInfoId)
        }
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    if (state.loading) {
        Loader()
    }
    if (state.success) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }
    AddScreens(title = "Add health information", back = { navController.popBackStack() }) {
        item {
            OutlinedTextField(
                value = if (state.date.isEqual(LocalDate.of(1000, 1, 1))) {
                    ""
                } else {
                    state.date.toString()
                },
                onValueChange = {},
                label = {
                    Text(text = "Date")
                },
                singleLine = true,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
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
            CustomTextField(
                value = state.explanation,
                onValueChange = explanationChange,
                label = "Explanation",
                placeHolder = "Explanation",
                errorText = state.explanationError,
                singleLine = false,
                minLines = 3
            )
        }
        item {
            CustomTextField(
                value = state.note,
                onValueChange = noteChange,
                label = "Note",
                placeHolder = "Note",
                errorText = state.noteError,
                singleLine = false,
                minLines = 3
            )
        }
        item {
            Button(
                onClick = {
                    viewModel.onEvent(AddHealthInfoEvent.Add(healthInfoId))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = if (healthInfoId != null) "Save" else "Add",
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
        item {
            CustomDatePicker(
                showDatePicker = showDatePicker,
                dismissDatePicker = {
                    showDatePicker = false
                },
                dateChange = dateChange
            )
        }
    }
}