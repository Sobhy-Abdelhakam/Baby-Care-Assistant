package dev.sobhy.babycareassistant.authentication.persentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.navigation.AuthenticationRoutes
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.Loader
import dev.sobhy.babycareassistant.ui.composable.PasswordTextField


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.loginState.collectAsState()
    if (state.isLoading){
        Loader()
    }
    LaunchedEffect(key1 = state.isSuccess){
        if (state.isSuccess){
            navController.popBackStack()
            navController.navigate(ScreenRoutes.Home.route)
        }
    }
//    if (state.isSuccess) {
//        navController.popBackStack()
//        navController.navigate(ScreenRoutes.Home.route)
//    }

    LazyColumn(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(18.dp),
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(top = 25.dp)
                    .padding(30.dp)
            )
        }
        item {
            CustomTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEvent(LoginUiEvent.UpdateEmail(it)) },
                label = "Email",
                placeHolder = "Email",
                errorText = viewModel.emailError,
                keyboardType = KeyboardType.Email
            )
        }
        item {
            PasswordTextField(
                password = viewModel.password,
                onPasswordChange = { viewModel.onEvent(LoginUiEvent.UpdatePassword(it)) },
                passwordError = viewModel.passwordError,
                onDoneClick = { viewModel.onEvent(LoginUiEvent.Login) },
            )
        }

        item {
            Button(
                onClick = { viewModel.onEvent(LoginUiEvent.Login) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.extraSmall,
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }
        state.error?.let {
            item {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Don\'t have an account?")
                TextButton(onClick = {
                    navController.navigate(AuthenticationRoutes.REGISTER.route)
                }) {
                    Text(text = "Sign up", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}