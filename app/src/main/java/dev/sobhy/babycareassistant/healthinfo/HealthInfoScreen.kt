package dev.sobhy.babycareassistant.healthinfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.FeaturesScreenContent

@Composable
fun HealthInfoScreen(
    navController: NavController,
    viewModel: HealthInfoViewModel = hiltViewModel(),
) {
    val state by viewModel.healthInfoState.collectAsStateWithLifecycle()
    FeaturesScreenContent(
        isLoading = state.loading,
        data = state.data,
        errorMessage = state.error,
        screenTitle = "Health Information",
        buttonText = "Add Health Information",
        itemContent = {
            HealthInfoItem(
                healthInfoDate = it.healthInfoDate,
                healthInfoExplanation = it.healthInfoExplanation,
                healthInfoNote = it.healthInfoNote,
                editButtonClick = {
                    navController.navigate(ScreenRoutes.AddHealthInformation.route + "/${it.healthInfoId}")
                },
                deleteButtonClick = {
                    viewModel.deleteHealthInfo(it.healthInfoId)
                }
            )
        },
        emptyMessage = "No Health Information found.",
        addButtonClick = {
            navController.navigate(ScreenRoutes.AddHealthInformation.route + "/${null}")
        },
        backButtonClick = {
            navController.popBackStack()
        })


}

@Composable
fun HealthInfoItem(
    healthInfoDate: String,
    healthInfoExplanation: String,
    healthInfoNote: String,
    editButtonClick: () -> Unit,
    deleteButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(1.dp, color = Color.Gray),
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Date: $healthInfoDate")
            Row {
                IconButton(
                    onClick = editButtonClick,
                    colors = IconButtonColors(
                        containerColor = Color(0xFF0C98FD),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF0C98FD),
                        disabledContentColor = Color.White
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Icon",
                    )
                }
                IconButton(
                    onClick = deleteButtonClick,
                    colors = IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.error,
                        disabledContentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Edit Icon",
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = "Explanation ", modifier = Modifier.weight(2f), color = Color.Gray)
            Text(text = healthInfoExplanation, modifier = Modifier.weight(5f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = "Note ", modifier = Modifier.weight(2f), color = Color.Gray)
            Text(text = healthInfoNote, modifier = Modifier.weight(5f))
        }
    }
}