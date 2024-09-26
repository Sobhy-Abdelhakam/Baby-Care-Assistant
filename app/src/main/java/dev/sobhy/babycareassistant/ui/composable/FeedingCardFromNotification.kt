package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FeedingCardFromNotification(
    breastFeed: BreastFeed,
    indexOfColorChange: Int,
    modifier: Modifier = Modifier,
    later: () -> Unit,
    done: () -> Unit
) {
    Dialog(
        onDismissRequest = {  },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Card(
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, color = Color.Gray),
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(text = "Breastfeeding Data", modifier = Modifier.padding(8.dp))
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
                Text(text = "${breastFeed.date}, ${breastFeed.day}")
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
                Text(text = "Number of feedings/day", color = Color.Gray)
                Text(text = breastFeed.numberOfFeedingsPerDay.toString())
            }
            breastFeed.timeOfTimes.forEachIndexed { index, time ->
                val timeFormat =
                    LocalTime.parse(time.time).format(DateTimeFormatter.ofPattern("hh:mm a"))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Time ${index + 1}",
                        color = if (index == indexOfColorChange) Color.Green else Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Time: ", color = Color.Gray)
                        Text(text = timeFormat)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Amount of milk", color = Color.Gray)
                        Text(text = time.amountOfMilk.toString())
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
                    Text(text = "Close")
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

}