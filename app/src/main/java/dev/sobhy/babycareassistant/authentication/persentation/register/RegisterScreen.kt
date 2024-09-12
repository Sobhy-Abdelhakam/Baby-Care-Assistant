package dev.sobhy.babycareassistant.authentication.persentation.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sobhy.babycareassistant.navigation.AuthenticationRoutes
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.Loader
import dev.sobhy.babycareassistant.ui.composable.PasswordTextField
import java.time.LocalDate
import java.time.Year

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.registerState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val imageChange = remember<(Uri) -> Unit> {
        { viewModel.onEvent(RegisterUiEvent.ImageChange(it)) }
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = {
                it?.let {
                    imageChange(it)
                }
            },
        )

    // lambda remembers for each event
    val nameChange =
        remember<(String) -> Unit> {
            { viewModel.onEvent(RegisterUiEvent.FullNameChange(it)) }
        }
    val emailChange =
        remember<(String) -> Unit> {
            { viewModel.onEvent(RegisterUiEvent.EmailChange(it)) }
        }
    val genderChange =
        remember<(String) -> Unit> {
            { viewModel.onEvent(RegisterUiEvent.GenderChange(it)) }
        }
    val dateChange =
        remember<(LocalDate) -> Unit> {
            { viewModel.onEvent(RegisterUiEvent.DOBChange(it)) }
        }
    val passwordChange =
        remember<(String) -> Unit> {
            { viewModel.onEvent(RegisterUiEvent.PasswordChange(it)) }
        }
    val confirmPasswordChange =
        remember<(String) -> Unit> {
            { viewModel.onEvent(RegisterUiEvent.ConfirmPasswordChange(it)) }
        }

    if (state.success) {
        navController.navigate(ScreenRoutes.Home.route){
            popUpTo(AuthenticationRoutes.LOGIN.route){
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    if (state.isLoading) {
        Loader()
    }
    Scaffold(
        topBar = {
            TopAppBar(
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
                title = {
                    Text(
                        text = "Sign up",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                )
            )
        },
    ) { paddingValue ->
        val items = listOf("Male", "Female")
        var expanded by remember { mutableStateOf(false) }
        LazyColumn(
            modifier = Modifier
                .padding(paddingValue)
                .padding(18.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Baby Photo")
                        IconButton(
                            onClick = {
                                galleryLauncher.launch("image/*")
                            },
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.3f
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "load image",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                    }
                    state.imageError?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
            item {
                CustomTextField(
                    value = state.fullName,
                    onValueChange = nameChange,
                    label = "Full Baby name",
                    placeHolder = "Full Baby name",
                    errorText = state.fullNameError
                )
            }
            item {
                CustomTextField(
                    value = state.email,
                    onValueChange = emailChange,
                    label = "Email",
                    placeHolder = "Email",
                    errorText = state.emailError
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value =
                        if (state.dateOfBirth.isEqual(LocalDate.of(1000, 1, 1))) {
                            ""
                        } else {
                            state.dateOfBirth.toString()
                        },
                        onValueChange = {},
                        modifier = Modifier.weight(3f),
                        label = {
                            Text(text = "Birthdate")
                        },
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Calendar Icon",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        isError = state.dateOfBirthError != null,
                        supportingText = {
                            if (state.dateOfBirthError != null) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = state.dateOfBirthError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Start,
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    OutlinedTextField(
                        value = state.age.toString(),
                        onValueChange = {},
                        label = {
                            Text(text = "Age")
                        },
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.weight(1f),
                        supportingText = { },
                    )
                }
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = state.gender,
                        onValueChange = { },
                        label = { Text("Gender") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = selectionOption)
                                },
                                onClick = {
                                    genderChange(selectionOption)
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            item {
                PasswordTextField(
                    password = state.password,
                    onPasswordChange = passwordChange,
                    passwordError = state.passwordError,
                    onDoneClick = {
                        ImeAction.Next
                    }
                )
            }
            item {
                PasswordTextField(
                    password = state.confirmPassword,
                    onPasswordChange = confirmPasswordChange,
                    label = "Confirm Password",
                    placeHolder = "Confirm Password",
                    passwordError = state.confirmPasswordError,
                    onDoneClick = {
                        viewModel.onEvent(RegisterUiEvent.Register)
                    }
                )
            }
            item {
                Button(
                    onClick = {
                        viewModel.onEvent(RegisterUiEvent.Register)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
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
                    Text(text = "Already have an account, ")
                    TextButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = "Sign in", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        CustomDatePicker(
            showDatePicker = showDatePicker,
            dismissDatePicker = {
                showDatePicker = false
            },
            dateChange = {date ->
                dateChange(date)
                viewModel.onEvent(
                    RegisterUiEvent.AgeChange(
                        (Year.now().value - date.year) + 1
                    )
                )
            },
        )
    }
}
