package dev.sobhy.babycareassistant.breastfeeding

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
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.FeaturesScreenContent
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BreastFeedingScreen(
    navController: NavController,
    viewModel: FeedingViewModel = hiltViewModel(),
) {
    val state by viewModel.feedingState.collectAsStateWithLifecycle()
    FeaturesScreenContent(
        isLoading = state.loading,
        data = state.data,
        errorMessage = state.error,
        screenTitle = "Breastfeeding",
        buttonText = "Add Breastfeeding Data",
        itemContent = {
            BreastFeedingItem(
                feedingDate = it.date,
                feedingDay = it.day,
                numberOfFeedingPerDay = it.numberOfFeedingsPerDay.toString(),
                amountOfMilkPerTime = it.amountOfMilkPerTime.toString(),
                timeOfTimes = it.timeOfTimes,
                editButtonClick = {
                    navController.navigate(ScreenRoutes.AddBreastfeed.route + "/${it.id}")
                },
                deleteButtonClick = {
                    viewModel.deleteFeeding(it.id)
                }
            )
        },
        addButtonClick = {
            navController.navigate(ScreenRoutes.AddBreastfeed.route + "/${null}")
        },
        backButtonClick = { navController.popBackStack() },
        emptyMessage = "No Breastfeeding Data Found"
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreastFeedingItem(
    feedingDate: String,
    feedingDay: String,
    numberOfFeedingPerDay: String,
    amountOfMilkPerTime: String,
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
            .padding(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Breastfeeding Data")
            Row {
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
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Date", color = Color.Gray)
            Text(text = "$feedingDate, $feedingDay")
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
            Text(text = numberOfFeedingPerDay)
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
            Text(text = "Amount of milk per time", color = Color.Gray)
            Text(text = "$amountOfMilkPerTime mm")
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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