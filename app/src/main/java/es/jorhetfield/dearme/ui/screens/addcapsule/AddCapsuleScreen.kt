package es.jorhetfield.dearme.ui.screens.addcapsule

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import java.util.TimeZone
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import es.jorhetfield.dearme.ui.components.BaseScaffold
import es.jorhetfield.dearme.ui.theme.Dimens
import java.io.File
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCapsuleScreen(
    onNavigateBack: () -> Unit,
    onCapsuleSaved: () -> Unit,
    viewModel: AddCapsuleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Gallery launcher — no permission needed on Android 13+ (backport via Jetpack)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onAttachedFileAdded(
                AttachedFile(
                    name = uri.lastPathSegment ?: "foto",
                    type = FileType.PHOTO,
                    uri = uri
                )
            )
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            val uri = uiState.pendingCameraUri
            if (uri != null) {
                viewModel.onAttachedFileAdded(
                    AttachedFile(
                        name = "foto_${System.currentTimeMillis()}.jpg",
                        type = FileType.PHOTO,
                        uri = uri
                    )
                )
            }
        }
        viewModel.setPendingCameraUri(null)
    }

    // Camera permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        if (granted) {
            val uri = createCameraUri(context)
            viewModel.setPendingCameraUri(uri)
            cameraLauncher.launch(uri)
        }
    }

    fun launchCamera() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasCameraPermission) {
            val uri = createCameraUri(context)
            viewModel.setPendingCameraUri(uri)
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onCapsuleSaved()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(uiState.error!!)
            viewModel.clearError()
        }
    }

    BackHandler {
        if (uiState.hasChanges) {
            viewModel.onShowBackDialog(true)
        } else {
            onNavigateBack()
        }
    }

    BaseScaffold(
        snackbarHostState = snackbarHostState,
        snackbarAtTop = true,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nueva Cápsula",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.hasChanges) {
                            viewModel.onShowBackDialog(true)
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Show help */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Información"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Área de redacción (ocupa el espacio principal)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.Padding.generous,
                        vertical = Dimens.Padding.comfortable
                    )
            ) {
                TextField(
                    value = uiState.message,
                    onValueChange = { viewModel.onMessageChanged(it) },
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Text(
                            "Querido yo del futuro, hoy me siento...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 32.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 32.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
            }

            // Carrusel de adjuntos (si hay archivos)
            if (uiState.attachedFiles.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.Padding.generous),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    thickness = 0.5.dp
                )

                LazyRow(
                    contentPadding = PaddingValues(
                        horizontal = Dimens.Padding.generous,
                        vertical = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.attachedFiles) { file ->
                        InputChip(
                            selected = false,
                            onClick = { },
                            label = { Text(file.name) },
                            leadingIcon = {
                                if (file.type == FileType.PHOTO && file.uri != null) {
                                    AsyncImage(
                                        model = file.uri,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(InputChipDefaults.AvatarSize)
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Icon(
                                        imageVector = when (file.type) {
                                            FileType.AUDIO -> Icons.Filled.Phone
                                            FileType.PHOTO -> Icons.Filled.CameraAlt
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { viewModel.onAttachedFileRemoved(file) },
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Eliminar",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = InputChipDefaults.inputChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }

            // Separador visual antes del panel de herramientas
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                thickness = 0.5.dp
            )

            // Panel de herramientas
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.Padding.generous)
                    ) {
                        DateSelector(
                            selectedDateMillis = uiState.selectedDateMillis,
                            selectedHour = uiState.selectedHour,
                            selectedMinute = uiState.selectedMinute,
                            onDateClick = { viewModel.onShowDatePicker(true) },
                            onSurpriseClick = {
                                val now = System.currentTimeMillis()
                                val maxDays = 30L * 24 * 60 * 60 * 1000
                                val randomMillis = now + Random.nextLong(1, maxDays)
                                val cal = java.util.Calendar.getInstance().apply {
                                    timeInMillis = randomMillis
                                }

                                // Random hour between 9 AM and 9 PM (21:00)
                                val randomHour = Random.nextInt(9, 22) // 9-21
                                val randomMinute = Random.nextInt(0, 60) // 0-59

                                viewModel.onDateMillisSelected(cal.apply {
                                    set(java.util.Calendar.HOUR_OF_DAY, 0)
                                    set(java.util.Calendar.MINUTE, 0)
                                    set(java.util.Calendar.SECOND, 0)
                                    set(java.util.Calendar.MILLISECOND, 0)
                                }.timeInMillis)
                                viewModel.onTimeSelected(randomHour, randomMinute)
                            }
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.Padding.generous),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                        thickness = 0.5.dp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.Padding.generous),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MultimediaActionButton(
                                icon = Icons.Outlined.Add,
                                label = "Foto",
                                onClick = { viewModel.onShowPhotoSourcePicker(true) },
                                modifier = Modifier.weight(1f)
                            )
                            MultimediaActionButton(
                                icon = Icons.Outlined.Phone,
                                label = "Voz",
                                onClick = { /* TODO: Microphone */ },
                                modifier = Modifier.weight(1f)
                            )
                            MultimediaActionButton(
                                icon = Icons.Outlined.FavoriteBorder,
                                label = "Emoción",
                                onClick = { /* TODO: Mood */ },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        ExpressiveSealButton(
                            onClick = {
                                if (!uiState.isSealing) {
                                    viewModel.onSealCapsule()
                                }
                            },
                            isLoading = uiState.isSealing,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    // Photo Source Picker Bottom Sheet
    if (uiState.showPhotoSourcePicker) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { viewModel.onShowPhotoSourcePicker(false) },
            sheetState = sheetState
        ) {
            ListItem(
                headlineContent = { Text("Usar cámara") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = null
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.onShowPhotoSourcePicker(false)
                        launchCamera()
                    }
            )
            ListItem(
                headlineContent = { Text("Elegir de la galería") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Filled.PhotoLibrary,
                        contentDescription = null
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable {
                        viewModel.onShowPhotoSourcePicker(false)
                        galleryLauncher.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/*")))
                    }
            )
        }
    }

    // Date Picker Dialog
    if (uiState.showDatePicker) {
        // Get today at 00:00:00 UTC for proper comparison with DatePicker
        val todayCalendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val todayMillis = todayCalendar.timeInMillis

        val selectableDates = remember(todayMillis) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= todayMillis
                }
            }
        }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedDateMillis ?: todayMillis,
            yearRange = IntRange(java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(java.util.Calendar.YEAR), 2100),
            selectableDates = selectableDates
        )
        DatePickerDialog(
            onDismissRequest = { viewModel.onShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedMillis ->
                        viewModel.onDateMillisSelected(selectedMillis)
                        viewModel.onShowDatePicker(false)
                        viewModel.onShowTimePicker(true)
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowDatePicker(false) }) {
                    Text("Cancelar")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                headlineContentColor = MaterialTheme.colorScheme.onSurface,
                weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                yearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                currentYearContentColor = MaterialTheme.colorScheme.primary,
                selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                dayContentColor = MaterialTheme.colorScheme.onSurface,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                todayContentColor = MaterialTheme.colorScheme.primary,
                todayDateBorderColor = MaterialTheme.colorScheme.primary
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time Picker Dialog
    if (uiState.showTimePicker) {
        val now = java.util.Calendar.getInstance()
        val defaultHour = uiState.selectedHour ?: now.get(java.util.Calendar.HOUR_OF_DAY)
        val defaultMinute = uiState.selectedMinute ?: now.get(java.util.Calendar.MINUTE)

        val timePickerState = rememberTimePickerState(
            initialHour = defaultHour,
            initialMinute = defaultMinute
        )

        AlertDialog(
            onDismissRequest = { viewModel.onShowTimePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onTimeSelected(timePickerState.hour, timePickerState.minute)
                    viewModel.onShowTimePicker(false)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowTimePicker(false) }) {
                    Text("Cancelar")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectorColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        periodSelectorBorderColor = MaterialTheme.colorScheme.outline,
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        )
    }

    // Back Confirmation Dialog
    if (uiState.showBackDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowBackDialog(false) },
            title = { Text("¿Descartar cambios?") },
            text = { Text("Tienes cambios sin guardar. ¿Seguro que quieres salir?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onShowBackDialog(false)
                    onNavigateBack()
                }) { Text("Descartar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowBackDialog(false) }) { Text("Cancelar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun createCameraUri(context: Context): Uri {
    val photoDir = File(context.cacheDir, "camera_photos").also { it.mkdirs() }
    val photoFile = File(photoDir, "photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        photoFile
    )
}

@Composable
fun MultimediaActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = true
                is PressInteraction.Release -> isPressed = false
                is PressInteraction.Cancel -> isPressed = false
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "button_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed) 4.dp.value else 8.dp.value,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "button_shadow"
    )

    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = shadowElevation.dp,
                shape = MaterialTheme.shapes.medium,
                clip = false
            )
            .scale(scale),
        shape = MaterialTheme.shapes.medium,
        interactionSource = interactionSource
    ) {
        Column(
            modifier = Modifier.size(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ExpressiveSealButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = !isLoading && true
                is PressInteraction.Release -> isPressed = false
                is PressInteraction.Cancel -> isPressed = false
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && !isLoading) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "seal_button_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed && !isLoading) 3.dp.value else 8.dp.value,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "seal_button_shadow"
    )

    ExtendedFloatingActionButton(
        text = { Text("Sellar cápsula") },
        icon = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.5.dp
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
            }
        },
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        modifier = modifier
            .shadow(
                elevation = shadowElevation.dp,
                shape = MaterialTheme.shapes.large,
                clip = false
            )
            .scale(scale),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        interactionSource = interactionSource
    )
}
