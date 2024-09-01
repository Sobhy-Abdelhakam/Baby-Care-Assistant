package dev.sobhy.babycareassistant.vaccination

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.FeaturesScreenContent
import dev.sobhy.babycareassistant.ui.composable.Loader
import dev.sobhy.babycareassistant.vaccination.data.Vaccination

@Composable
fun VaccinationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: VaccinationViewModel = hiltViewModel(),
) {
    val state by viewModel.vaccinations.collectAsState()
    FeaturesScreenContent(
        isLoading = state.loading,
        data = state.data,
        errorMessage = state.error,
        screenTitle = "Vaccinations",
        buttonText = "Add Vaccination Data",
        itemContent = {
            VaccineItem(
                vaccineName = it.name,
                date = it.date,
                day = it.day,
                code = it.code,
                ageGroup = it.ageGroup,
                reason = it.reason,
                notes = it.notes
            )
        },
        emptyMessage = "No vaccinations found.",
        addButtonClick = {
                         navController.navigate(ScreenRoutes.AddVaccination.route)
                         },
        backButtonClick = {
            navController.popBackStack()
        })

}

@Composable
fun VaccineItem(
    vaccineName: String,
    date: String,
    day: String,
    code: String,
    ageGroup: String,
    reason: String,
    notes: String,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(1.dp, color = Color.Gray),
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = vaccineName, modifier = Modifier.padding(2.dp))
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "", tint = Color(0xFF68D275)
            )
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
            Text(text = "Date", color = Color.Gray)
            Text(text = "$date, $day")
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
            Text(text = code)
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
            Text(text = ageGroup)
        }
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Reason for vaccination:",
                color = Color.Gray,
                modifier = Modifier.padding(2.dp)
            )
            Text(text = reason, modifier = Modifier.padding(2.dp))
        }
        if (notes.isNotEmpty()){
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Other notes:", color = Color.Gray, modifier = Modifier.padding(2.dp))
                Text(text = notes, modifier = Modifier.padding(2.dp))
            }
        }

    }
}