package dev.sobhy.babycareassistant.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import dev.sobhy.babycareassistant.authentication.domain.UserProfile
import dev.sobhy.babycareassistant.navigation.AuthenticationRoutes
import dev.sobhy.babycareassistant.ui.composable.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            when {
                state.isLoading -> {
                    Loader()
                }

                state.error != null -> {
                    Text(
                        text = state.error ?: "Unknown error",
                        fontSize = 25.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.user != null -> {
                    ProfileContent(
                        userProfile = state.user!!,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        logout = {
                            viewModel.signOut()
                            navController.navigate(AuthenticationRoutes.LOGIN.route) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }


    }
}

@Composable
fun ProfileContent(
    userProfile: UserProfile,
    modifier: Modifier = Modifier,
    logout: () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = userProfile.profileImage,
            contentDescription = "User Image",
            imageLoader = ImageLoader.Builder(context).crossfade(true).build(),
            modifier = Modifier
                .padding(16.dp)
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = userProfile.fullName,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp)
        )
        TextField(
            value = userProfile.email,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFA5CFD5),
                unfocusedContainerColor = Color(0xFFA5CFD5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
        Text(
            text = "Birthdate",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp)
        )
        TextField(
            value = userProfile.dateOfBirth,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFA5CFD5),
                unfocusedContainerColor = Color(0xFFA5CFD5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(end = 4.dp).weight(1f)) {
                Text(
                    text = "Gender",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                TextField(
                    value = userProfile.gender,
                    onValueChange = {},
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFA5CFD5),
                        unfocusedContainerColor = Color(0xFFA5CFD5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
            }
            Column(modifier = Modifier.padding(start = 4.dp).weight(1f)) {
                Text(
                    text = "Age",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                TextField(
                    value = userProfile.age.toString(),
                    onValueChange = {},
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFA5CFD5),
                        unfocusedContainerColor = Color(0xFFA5CFD5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = logout,
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Logout", modifier = Modifier.padding(8.dp))
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Logout Icon",
                modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
            )
        }
    }
}