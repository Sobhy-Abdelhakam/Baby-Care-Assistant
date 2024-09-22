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
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiapersCardFromNotification(
    diapers: Diapers,
    later: () -> Unit,
    done: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(1.dp, color = Color.Gray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
    ) {
        Text(text = "Date: ${diapers.date}, ${diapers.day}", modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .padding(horizontal = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Number of diaper changes", color = Color.Gray)
            Text(text = diapers.numberOfDiapersChange.toString())
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            diapers.timesOfDiapersChange.forEachIndexed { index, time ->
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
                onClick = later,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text(text = "Later")
            }
            Button(
                onClick = done,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(text = "Done")
            }
        }
    }
}