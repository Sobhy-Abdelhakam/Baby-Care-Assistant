package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.sobhy.babycareassistant.authentication.persentation.register.RegisterUiEvent
import java.time.LocalDate
import java.time.Year

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    showDatePicker: Boolean,
    dismissDatePicker: () -> Unit,
    dateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDatePicker.not()) return
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = dismissDatePicker,
        confirmButton = {
            TextButton(
                onClick = {
                    dismissDatePicker()
                    val time = datePickerState.selectedDateMillis
                    val convertDate =
                        LocalDate.ofEpochDay(time!! / (1000 * 60 * 60 * 24))
                    dateChange(convertDate)
//                    viewModel.onEvent(
//                        RegisterUiEvent.AgeChange(
//                            (Year.now().value - convertDate.year) + 1
//                        )
//                    )
                },
                enabled = datePickerState.selectedDateMillis != null,
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissDatePicker,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text(text = "Cancel")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}