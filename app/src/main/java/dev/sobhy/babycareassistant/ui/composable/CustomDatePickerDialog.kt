package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    showDatePicker: Boolean,
    dismissDatePicker: () -> Unit,
    dateChange: (LocalDate) -> Unit,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
) {
    if (showDatePicker.not()) return
    val datePickerState = rememberDatePickerState(
        yearRange = yearRange,
        selectableDates = selectableDates
    )
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