package dev.sobhy.babycareassistant.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.IconsCardInMainScreen

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.padding(top = 32.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(18.dp),
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconsCardInMainScreen(
                            cardColor = Color(0xFF6F4DCC),
                            cardClick = { navController.navigate(ScreenRoutes.VaccinationScreen.route) },
                            iconDrawablePath = R.drawable.vaccination_icon,
                            cardText = "Vaccinations",
                            modifier = Modifier.weight(1f)
                        )
                        IconsCardInMainScreen(
                            cardColor = Color(0xFFFB8236),
                            cardClick = {
                                navController.navigate(ScreenRoutes.Breastfeed.route)
                                        },
                            iconDrawablePath = R.drawable.breastfeeding_icon,
                            cardText = "Breastfeeding",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconsCardInMainScreen(
                            cardColor = Color(0xFFF9CD4D),
                            cardClick = {
                                navController.navigate(ScreenRoutes.BabyGrowth.route)
                                        },
                            iconDrawablePath = R.drawable.baby_growth_icon,
                            cardText = "Baby Growth",
                            modifier = Modifier.weight(1f)
                        )
                        IconsCardInMainScreen(
                            cardColor = Color(0xFF4ECBEE),
                            cardClick = {
                                navController.navigate(ScreenRoutes.BabySleep.route)
                                        },
                            iconDrawablePath = R.drawable.sleep_icon,
                            cardText = "Baby sleep",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconsCardInMainScreen(
                            cardColor = Color(0xFF50CFBF),
                            cardClick = {
                                navController.navigate(ScreenRoutes.Diapers.route)
                                        },
                            iconDrawablePath = R.drawable.diaper_icon,
                            cardText = "Diapers",
                            modifier = Modifier.weight(1f)
                        )
                        IconsCardInMainScreen(
                            cardColor = Color(0xFF90DC73),
                            cardClick = {
                                navController.navigate(ScreenRoutes.HealthInformation.route)
                                        },
                            iconDrawablePath = R.drawable.health_info_icon,
                            cardText = "Health information",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.navigate(ScreenRoutes.Notification.route) }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = { navController.navigate(ScreenRoutes.Profile.route) }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(30.dp)
                )
            }

        }
    }
}