package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeHolder: String,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        label = { Text(text = label) },
        placeholder = { Text(text = placeHolder) },
        singleLine = singleLine,
        enabled = !readOnly,
        isError = errorText != null,
        supportingText = {
            if (errorText != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Start,
                )
            }
        },
//        trailingIcon = {
//            if (emailError != null) {
//                Icon(
//                    Icons.Filled.Error,
//                    "error",
//                    tint = MaterialTheme.colorScheme.error,
//                )
//            }
//        },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next,
        ),
        minLines = minLines,
    )
}