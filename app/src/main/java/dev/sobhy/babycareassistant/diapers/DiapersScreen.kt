package dev.sobhy.babycareassistant.diapers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DiapersScreen(
    navController: NavController,
    viewModel: DiapersViewModel = hiltViewModel(),
) {
    val state by viewModel.diapersState.collectAsStateWithLifecycle()
    FeaturesScreenContent(
        isLoading = state.loading,
        data = state.data,
        errorMessage = state.error,
        screenTitle = "Diapers",
        buttonText = "Add Diapers Data",
        itemContent = {
            DiaperItem(
                diaperDate = it.date,
                diaperDay = it.day,
                numberOfDiaperChange = it.numberOfDiapersChange.toString(),
                timeOfTimes = it.timesOfDiapersChange,
                editButtonClick = {
                    navController.navigate(ScreenRoutes.AddDiaper.route + "/${it.id}")
                },
                deleteButtonClick = {
                    viewModel.deleteDiaper(it.id)
                })
        },
        addButtonClick = {
            navController.navigate(ScreenRoutes.AddDiaper.route + "/${null}")
        },
        backButtonClick = { navController.popBackStack() },
        emptyMessage = "No Diapers Data Found"
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiaperItem(
    diaperDate: String,
    diaperDay: String,
    numberOfDiaperChange: String,
    timeOfTimes: List<String>,
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
            .padding(2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Date: $diaperDate, $diaperDay", modifier = Modifier.weight(2.5f))
            Row(modifier = Modifier.weight(1.5f), horizontalArrangement = Arrangement.End) {
                IconButton(
                    onClick = editButtonClick,
                    modifier= Modifier
                        .padding(4.dp)
                        .background(
                            color = Color(0xFF0C98FD),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .size(35.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Icon",
                    )
                }
                IconButton(
                    onClick = deleteButtonClick,
                    modifier= Modifier
                        .padding(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .size(35.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                    ),
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
            Text(text = numberOfDiaperChange)
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            timeOfTimes.forEachIndexed { index, time ->
                val timeFormat =
                    LocalTime.parse(time).format(DateTimeFormatter.ofPattern("hh:mm a"))
                Row {
                    Text(text = "Time ${index + 1}    ", color = Color.Gray)
                    Text(text = timeFormat)
                }
            }
        }
    }
}