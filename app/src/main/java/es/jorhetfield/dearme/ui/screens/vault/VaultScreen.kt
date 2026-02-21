package es.jorhetfield.dearme.ui.screens.vault

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.jorhetfield.dearme.domain.extension.formatUnlockTime
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.ui.components.BaseScaffold
import es.jorhetfield.dearme.ui.components.ErrorDialog
import es.jorhetfield.dearme.ui.theme.CapsuleDark1
import es.jorhetfield.dearme.ui.theme.CapsuleDark10
import es.jorhetfield.dearme.ui.theme.CapsuleDark2
import es.jorhetfield.dearme.ui.theme.CapsuleDark3
import es.jorhetfield.dearme.ui.theme.CapsuleDark4
import es.jorhetfield.dearme.ui.theme.CapsuleDark5
import es.jorhetfield.dearme.ui.theme.CapsuleDark6
import es.jorhetfield.dearme.ui.theme.CapsuleDark7
import es.jorhetfield.dearme.ui.theme.CapsuleDark8
import es.jorhetfield.dearme.ui.theme.CapsuleDark9
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText1
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText10
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText2
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText3
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText4
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText5
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText6
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText7
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText8
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText9
import es.jorhetfield.dearme.ui.theme.CapsuleLight1
import es.jorhetfield.dearme.ui.theme.CapsuleLight10
import es.jorhetfield.dearme.ui.theme.CapsuleLight2
import es.jorhetfield.dearme.ui.theme.CapsuleLight3
import es.jorhetfield.dearme.ui.theme.CapsuleLight4
import es.jorhetfield.dearme.ui.theme.CapsuleLight5
import es.jorhetfield.dearme.ui.theme.CapsuleLight6
import es.jorhetfield.dearme.ui.theme.CapsuleLight7
import es.jorhetfield.dearme.ui.theme.CapsuleLight8
import es.jorhetfield.dearme.ui.theme.CapsuleLight9
import es.jorhetfield.dearme.ui.theme.CapsuleLightText1
import es.jorhetfield.dearme.ui.theme.CapsuleLightText10
import es.jorhetfield.dearme.ui.theme.CapsuleLightText2
import es.jorhetfield.dearme.ui.theme.CapsuleLightText3
import es.jorhetfield.dearme.ui.theme.CapsuleLightText4
import es.jorhetfield.dearme.ui.theme.CapsuleLightText5
import es.jorhetfield.dearme.ui.theme.CapsuleLightText6
import es.jorhetfield.dearme.ui.theme.CapsuleLightText7
import es.jorhetfield.dearme.ui.theme.CapsuleLightText8
import es.jorhetfield.dearme.ui.theme.CapsuleLightText9
import es.jorhetfield.dearme.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun VaultScreen(
    onNavigateToAddCapsule: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCapsuleDetail: (String) -> Unit,
    viewModel: VaultViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    with(sharedTransitionScope) {
        BaseScaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Dear Me",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.onRefresh() },
                            enabled = !uiState.isRefreshing
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                                tint = if (uiState.isRefreshing) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                        IconButton(
                            onClick = onNavigateToProfile,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Perfil",
                                    modifier = Modifier.padding(8.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToAddCapsule,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir cápsula")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Filter Segmented Buttons - Material 3 Style
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = Dimens.Padding.generous,
                            vertical = Dimens.Padding.comfortable
                        ),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val filterOptions = listOf(
                            CapsuleFilter.LOCKED to "Bloqueadas",
                            CapsuleFilter.READY to "Listas",
                            CapsuleFilter.OPENED to "Abiertas"
                        )
                        filterOptions.forEachIndexed { index, (filter, label) ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = filterOptions.size
                                ),
                                onClick = { viewModel.onFilterChanged(filter) },
                                selected = uiState.selectedFilter == filter,
                                icon = {
                                    if (uiState.selectedFilter == filter) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                label = { Text(label) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Content based on filter
                if (uiState.filteredCapsules.isEmpty()) {
                    EmptyVaultContent(
                        Modifier
                            .fillMaxSize()
                            .weight(1f),
                        message = when (uiState.selectedFilter) {
                            CapsuleFilter.LOCKED -> "No hay cápsulas bloqueadas"
                            CapsuleFilter.READY -> "No hay cápsulas listas para abrir"
                            CapsuleFilter.OPENED -> "No hay cápsulas abiertas"
                        }
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(Dimens.Padding.comfortable),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.lg),
                        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.lg),
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(
                            count = uiState.filteredCapsules.size,
                            key = { index -> uiState.filteredCapsules[index].id }
                        ) { index ->
                            CapsuleCard(
                                capsule = uiState.filteredCapsules[index],
                                onClick = { onNavigateToCapsuleDetail(uiState.filteredCapsules[index].id) },
                                colorIndex = index,
                                sharedTransitionScope = this@with,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                    }
                }
            }
        }
    }

    // Error dialog
    if (uiState.error != null) {
        ErrorDialog(
            message = uiState.error!!,
            onDismiss = {
                // Error handling can be added to CapsuleViewModel if needed
            }
        )
    }
}

@Composable
private fun EmptyVaultContent(
    modifier: Modifier = Modifier,
    message: String = "Tu bóveda está vacía"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(Dimens.Spacing.lg))
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(Dimens.Spacing.sm))
        Text(
            text = "Crea tu primera cápsula del tiempo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun CapsuleCard(
    modifier: Modifier = Modifier,
    capsule: Capsule,
    onClick: () -> Unit,
    colorIndex: Int = 0,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val unlockTimeText: String = remember(capsule.unlockDate) {
        capsule.unlockDate.formatUnlockTime()
    }

    val (backgroundColor, onBackgroundColor) = getCardColors(colorIndex)

    with(sharedTransitionScope) {
        Card(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "capsule_card_${capsule.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    boundsTransform = { _, _ -> tween(durationMillis = 400) }
                ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Padding.comfortable),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (capsule.isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Bloqueada",
                    tint = onBackgroundColor,
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = onBackgroundColor.copy(alpha = 0.8f),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = "¡Ábreme!",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = backgroundColor,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = unlockTimeText,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = onBackgroundColor,
                textAlign = TextAlign.Center
            )
        }
    }
    }
}

@Composable
private fun getCardColors(index: Int): Pair<Color, Color> {
    val isDarkMode = isSystemInDarkTheme()

    return if (isDarkMode) {
        when (index % 10) {
            0 -> Pair(CapsuleDark1, CapsuleDarkText1)
            1 -> Pair(CapsuleDark2, CapsuleDarkText2)
            2 -> Pair(CapsuleDark3, CapsuleDarkText3)
            3 -> Pair(CapsuleDark4, CapsuleDarkText4)
            4 -> Pair(CapsuleDark5, CapsuleDarkText5)
            5 -> Pair(CapsuleDark6, CapsuleDarkText6)
            6 -> Pair(CapsuleDark7, CapsuleDarkText7)
            7 -> Pair(CapsuleDark8, CapsuleDarkText8)
            8 -> Pair(CapsuleDark9, CapsuleDarkText9)
            else -> Pair(CapsuleDark10, CapsuleDarkText10)
        }
    } else {
        when (index % 10) {
            0 -> Pair(CapsuleLight1, CapsuleLightText1)
            1 -> Pair(CapsuleLight2, CapsuleLightText2)
            2 -> Pair(CapsuleLight3, CapsuleLightText3)
            3 -> Pair(CapsuleLight4, CapsuleLightText4)
            4 -> Pair(CapsuleLight5, CapsuleLightText5)
            5 -> Pair(CapsuleLight6, CapsuleLightText6)
            6 -> Pair(CapsuleLight7, CapsuleLightText7)
            7 -> Pair(CapsuleLight8, CapsuleLightText8)
            8 -> Pair(CapsuleLight9, CapsuleLightText9)
            else -> Pair(CapsuleLight10, CapsuleLightText10)
        }
    }
}

