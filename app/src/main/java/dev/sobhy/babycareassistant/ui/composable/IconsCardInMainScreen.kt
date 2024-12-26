package dev.sobhy.babycareassistant.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconsCardInMainScreen(
    cardColor: Color,
    cardClick: () -> Unit,
    iconDrawablePath: Int,
    cardText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = Color.White
        ),
        onClick = cardClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconDrawablePath),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(70.dp)
                    .padding(8.dp)
            )
            Text(
                text = cardText,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
    }
}