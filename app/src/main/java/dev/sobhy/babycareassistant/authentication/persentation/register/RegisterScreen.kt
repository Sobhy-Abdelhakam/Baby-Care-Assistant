package dev.sobhy.babycareassistant.authentication.persentation.register

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.alarm.data.repository.AlarmManagerHelper
import dev.sobhy.babycareassistant.navigation.AuthenticationRoutes
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.CustomDatePicker
import dev.sobhy.babycareassistant.ui.composable.CustomTextField
import dev.sobhy.babycareassistant.ui.composable.Loader
import dev.sobhy.babycareassistant.ui.composable.PasswordTextField
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId

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
    var changeImageBottomSheet by remember { mutableStateOf(false) }

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
    val context = LocalContext.current
    LaunchedEffect(state.success) {
        if (state.success) {
            val babyAgeInMonths = state.age
            viewModel.fetchFeedingSchedule(babyAgeInMonths) {feedingSchedule ->
                Log.d("MainActivity", "Feeding Schedule: $feedingSchedule")
                AlarmManagerHelper.scheduleFeedingNotification(
                    context,
                    feedingSchedule,
                    0 // Interval in hours
                )
            }
            navController.navigate(ScreenRoutes.Home.route) {
                popUpTo(AuthenticationRoutes.LOGIN.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
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
                        Image(
                            painter = if (state.image == null) {
                                painterResource(id = R.drawable.image_reg_icon)
                            } else {
                                rememberAsyncImagePainter(model = state.image)
                            },
                            contentDescription = "Baby Image",
                            modifier = Modifier
                                .size(100.dp)

                                .clip(shape = RoundedCornerShape(10.dp))
                                .clickable {
                                    if (state.image == null) {
                                        galleryLauncher.launch("image/*")
                                    } else {
                                        changeImageBottomSheet = true
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )
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
                if (changeImageBottomSheet) {
                    BabyImageBottomSheet(
                        dismissBottomSheet = {
                            changeImageBottomSheet = false
                        },
                        changeImageClick = {
                            galleryLauncher.launch("image/*")
                            changeImageBottomSheet = false
                        },
                        deleteImageClick = {
                            viewModel.onEvent(RegisterUiEvent.ImageChange(null))
                            changeImageBottomSheet = false
                        })
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
                    Text(text = "Already have an account, ")
                    TextButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = "Sign in", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        val today = LocalDate.now()
        val todayMillis = today.plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        CustomDatePicker(
            showDatePicker = showDatePicker,
            dismissDatePicker = {
                showDatePicker = false
            },
            yearRange = IntRange(Year.now().value - 10, Year.now().value),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= todayMillis
                }
            },
            dateChange = { date ->
                dateChange(date)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyImageBottomSheet(
    dismissBottomSheet: () -> Unit,
    changeImageClick: () -> Unit,
    deleteImageClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = dismissBottomSheet,
    ) {
        Text(
            text = "Baby photo",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = changeImageClick,
                    modifier =
                    Modifier
                        .size(70.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = CircleShape,
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Text(
                    text = "Gallery",
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = deleteImageClick,
                    modifier =
                    Modifier
                        .size(70.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = CircleShape,
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
                Text(
                    text = "Delete",
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
