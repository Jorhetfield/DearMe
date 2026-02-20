package es.jorhetfield.dearme.ui.screens.addcapsule

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType
import es.jorhetfield.dearme.ui.components.BaseScaffold
import es.jorhetfield.dearme.ui.components.ErrorDialog
import es.jorhetfield.dearme.ui.components.LoadingOverlay
import es.jorhetfield.dearme.ui.viewmodel.CapsuleViewModel
import es.jorhetfield.dearme.ui.viewmodel.UiState
import java.util.Calendar
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AddCapsuleScreen(
    onNavigateBack: () -> Unit,
    onCapsuleSaved: () -> Unit,
    viewModel: CapsuleViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var message by remember { mutableStateOf("") }
    var showBackDialog by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf<Int?>(null) }
    var selectedMinute by remember { mutableStateOf<Int?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isSealing by remember { mutableStateOf(false) }
    var attachedFiles by remember { mutableStateOf<List<AttachedFile>>(emptyList()) }
    var showErrorDialog by remember { mutableStateOf<String?>(null) }

    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    val hasChanges = message.isNotBlank()
    val unlockDate = remember(selectedDateMillis, selectedHour, selectedMinute) {
        if (selectedDateMillis != null && selectedHour != null && selectedMinute != null) {
            Calendar.getInstance().apply {
                timeInMillis = selectedDateMillis!!
                set(Calendar.HOUR_OF_DAY, selectedHour!!)
                set(Calendar.MINUTE, selectedMinute!!)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        } else null
    }
    BackHandler {
        if (hasChanges) showBackDialog = true else onNavigateBack()
    }

    with(sharedTransitionScope) {
        BaseScaffold(
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "fab_to_create_transition"),
                animatedVisibilityScope = animatedVisibilityScope,
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                enter = fadeIn(animationSpec = tween(500)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(500)
                ),
                exit = fadeOut(animationSpec = tween(500)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(500)
                ),
                boundsTransform = { _, _ ->
                    tween(durationMillis = 500)
                }
            ),
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
                            if (hasChanges) showBackDialog = true else onNavigateBack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
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
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    // Campo de texto (Editor limpio)
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .skipToLookaheadSize(),
                        placeholder = {
                            Text(
                                "Querido yo del futuro, hoy me siento...",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 32.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 32.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                }

                // Carrusel de adjuntos (si hay archivos)
                if (attachedFiles.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.skipToLookaheadSize()
                    ) {
                    items(attachedFiles) { file ->
                        InputChip(
                            selected = false,
                            onClick = { },
                            label = { Text(file.name) },
                            leadingIcon = {
                                Icon(
                                    imageVector = when(file.type) {
                                        FileType.AUDIO -> Icons.Filled.Phone
                                        FileType.PHOTO -> Icons.Filled.Person
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        attachedFiles = attachedFiles.filter { it != file }
                                    },
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Eliminar",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = InputChipDefaults.inputChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }

                // Panel de herramientas (Bottom Sheet Falso)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .skipToLookaheadSize(),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.95f),
                    tonalElevation = 3.dp
                ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Selector de fecha
                    DateSelector(
                        selectedDateMillis = selectedDateMillis,
                        selectedHour = selectedHour,
                        selectedMinute = selectedMinute,
                        onDateClick = { showDatePicker = true },
                        onSurpriseClick = {
                            // Fecha aleatoria
                            val now = System.currentTimeMillis()
                            val maxDays = 30L * 24 * 60 * 60 * 1000
                            val randomMillis = now + Random.nextLong(1, maxDays)
                            val cal = Calendar.getInstance().apply {
                                timeInMillis = randomMillis
                            }
                            selectedDateMillis = cal.apply {
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                            selectedHour = cal.get(Calendar.HOUR_OF_DAY)
                            selectedMinute = cal.get(Calendar.MINUTE)
                        }
                    )

                    // Barra de acciones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Multimedia (Izquierda)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { /* TODO: Camera */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Cámara"
                                )
                            }
                            IconButton(onClick = { /* TODO: Microphone */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Phone,
                                    contentDescription = "Micrófono"
                                )
                            }
                            IconButton(onClick = { /* TODO: Mood */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Emoción"
                                )
                            }
                        }

                        // Botón Sellar (Derecha)
                        ExtendedFloatingActionButton(
                            onClick = {
                                if (message.isNotBlank() && unlockDate != null) {
                                    isSealing = true
                                    val capsule = Capsule(
                                        id = UUID.randomUUID().toString(),
                                        userId = "temp_user",
                                        message = message,
                                        mediaPath = null,
                                        mediaType = MediaType.TEXT_ONLY,
                                        creationDate = System.currentTimeMillis(),
                                        unlockDate = unlockDate,
                                        isLocked = true,
                                        isOpened = false
                                    )
                                    viewModel.createCapsule(capsule)
                                    onCapsuleSaved()
                                }
                            },
                            icon = {
                                if (isSealing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = null
                                    )
                                }
                            },
                            text = { Text("Sellar") },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                    if (selectedHour == null) {
                        showTimePicker = true
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
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
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour ?: 12,
            initialMinute = selectedMinute ?: 0
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedHour = timePickerState.hour
                    selectedMinute = timePickerState.minute
                    showTimePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
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
    if (showBackDialog) {
        AlertDialog(
            onDismissRequest = { showBackDialog = false },
            title = { Text("¿Descartar cambios?") },
            text = { Text("Tienes cambios sin guardar. ¿Seguro que quieres salir?") },
            confirmButton = {
                TextButton(onClick = {
                    showBackDialog = false
                    onNavigateBack()
                }) { Text("Descartar") }
            },
            dismissButton = {
                TextButton(onClick = { showBackDialog = false }) { Text("Cancelar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    when (operationState) {
        is UiState.Loading -> {
            LoadingOverlay()
        }
        is UiState.Error -> {
            val errorMessage = (operationState as UiState.Error<Unit>).message
            if (showErrorDialog == null) {
                showErrorDialog = errorMessage
            }
        }
        is UiState.Success -> {
            isSealing = false
        }
        is UiState.Idle -> {
            isSealing = false
        }
    }

    if (showErrorDialog != null) {
        ErrorDialog(
            message = showErrorDialog!!,
            onDismiss = {
                showErrorDialog = null
                viewModel.clearError()
            }
        )
    }
}
