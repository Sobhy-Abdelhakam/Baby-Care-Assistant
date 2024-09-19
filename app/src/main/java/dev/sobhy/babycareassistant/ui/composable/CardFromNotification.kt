package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardFromNotification(
    title: String,
    dateAndDay: String,
    timesPerDay: String,
    modifier: Modifier = Modifier,
    amountOfMilk: String? = null,
    timesOfTimes: List<String>,
    dismiss: () -> Unit,
    onClick: () -> Unit
) {
    Dialog(
        onDismissRequest = dismiss
    ) {
        Card(
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.outlinedCardColors(),
            border = BorderStroke(1.dp, color = Color.Gray),
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            Text(text = title,
                modifier
                    .fillMaxWidth()
                    .padding(4.dp))

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
                Text(text = "Date", color = Color.Gray)
                Text(text = dateAndDay)
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
                Text(
                    text = if (amountOfMilk != null) "Number of feedings/day" else "Number of diaper changes",
                    color = Color.Gray
                )
                Text(text = timesPerDay)
            }
            amountOfMilk?.let {
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
                    Text(text = "Amount of milk per time", color = Color.Gray)
                    Text(text = "$it mm")
                }
            }


            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                timesOfTimes.forEachIndexed { index, time ->
                    val timeFormat =
                        LocalTime.parse(time).format(DateTimeFormatter.ofPattern("hh:mm a"))
                    Row {
                        Text(text = "Time ${index + 1}    ", color = Color.Gray)
                        Text(text = timeFormat)
                    }
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Button(
                    onClick = dismiss,
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text(text = "Later")
                }
                Button(
                    onClick = onClick,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(text = "Done")
                }
            }
        }
    }

}