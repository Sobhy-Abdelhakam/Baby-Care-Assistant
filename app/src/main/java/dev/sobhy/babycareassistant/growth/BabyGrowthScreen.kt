package dev.sobhy.babycareassistant.growth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.FeaturesScreenContent
import java.time.LocalDate

@Composable
fun BabyGrowthScreen(
    navController: NavController,
    viewModel: GrowthViewModel = hiltViewModel(),
) {
    val state by viewModel.growthState.collectAsStateWithLifecycle()
    FeaturesScreenContent(
        isLoading = state.loading,
        data = state.data,
        errorMessage = state.error,
        screenTitle = "Baby Growth",
        buttonText = "Add Baby Growth Data",
        itemContent = {
            val date = LocalDate.parse(it.babyGrowth.dateOfMeasurement)
            val day = date.dayOfWeek
            BabyGrowthItem(
                babyAge = it.babyGrowth.age,
                growthDate = it.babyGrowth.dateOfMeasurement,
                growthDay = day.toString(),
                babyWeight = it.babyGrowth.weight.toString(),
                babyHeight = it.babyGrowth.height.toString(),
                headCircumference = it.babyGrowth.headCircumference.toString(),
                status = it.status,
                editButtonClick = {
                    navController.navigate(ScreenRoutes.AddBabyGrowth.route + "/${it.babyGrowth.id}")
                },
                deleteButtonClick = {
                    viewModel.deleteGrowth(it.babyGrowth.id)
                }
            )
        },
        addButtonClick = {
            navController.navigate(ScreenRoutes.AddBabyGrowth.route + "/${null}")
        },
        backButtonClick = { navController.popBackStack() },
        emptyMessage = "No Growth Data Found"
    )
}


@Composable
fun BabyGrowthItem(
    babyAge: Int,
    growthDate: String,
    growthDay: String,
    babyWeight: String,
    babyHeight: String,
    headCircumference: String,
    status: String,
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
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Age: $babyAge months")
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
            Text(text = "Date of measurement", color = Color.Gray)
            Text(text = "$growthDate, $growthDay")
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
            Text(text = "Baby weight", color = Color.Gray)
            Text(text = "$babyWeight kg")
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
            Text(text = "Baby height", color = Color.Gray)
            Text(text = "$babyHeight cm")
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
            Text(text = "Head circumference", color = Color.Gray)
            Text(text = "$headCircumference cm")
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if (status.contains("normal")){
                Text(
                    text = status,
                    color = Color.Green,
                    modifier = Modifier.padding(end = 4.dp),
                    style = MaterialTheme.typography.titleMedium
                    )
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "",
                    tint = Color.Green
                )
            } else {
                Text(
                    text = status,
                    color = Color.Red,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "",
                    tint = Color.Red
                )
            }
        }
    }
}