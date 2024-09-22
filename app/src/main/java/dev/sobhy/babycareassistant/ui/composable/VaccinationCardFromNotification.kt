package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.sobhy.babycareassistant.vaccination.data.Vaccination

@Composable
fun VaccinationCardFromNotification(
    vaccination: Vaccination,
    modifier: Modifier = Modifier,
    done: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.extraSmall,
        border = BorderStroke(1.dp, color = Color.Gray),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Text(text = vaccination.name, modifier = Modifier.padding(8.dp))
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
            Text(text = "${vaccination.date}, ${vaccination.day}")
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
            Text(text = "Code", color = Color.Gray)
            Text(text = vaccination.code)
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
            Text(text = "Age Group", color = Color.Gray)
            Text(text = vaccination.ageGroup)
        }
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Reason for vaccination:",
                color = Color.Gray,
                modifier = Modifier.padding(2.dp)
            )
            Text(text = vaccination.reason, modifier = Modifier.padding(2.dp))
        }
        if (vaccination.notes.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Other notes:", color = Color.Gray, modifier = Modifier.padding(2.dp))
                Text(text = vaccination.notes, modifier = Modifier.padding(2.dp))
            }
        }
        Button(
            onClick = done,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = "Done")
        }
    }
}