package es.jorhetfield.dearme.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(containerColor),
    snackbarHostState: SnackbarHostState? = null,
    snackbarAtTop: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBarHeight = with(LocalDensity.current) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    }
    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }

    Box(modifier = modifier) {
        Scaffold(
            modifier = Modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = {
                Box(
                    modifier = Modifier.padding(bottom = if (navBarHeight > 0.dp) 16.dp else 0.dp)
                ) {
                    floatingActionButton()
                }
            },
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = containerColor,
            contentColor = contentColor,
            snackbarHost = {
                if (snackbarHostState != null && !snackbarAtTop) {
                    SnackbarHost(hostState = snackbarHostState)
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            content = content
        )

        if (snackbarHostState != null && snackbarAtTop) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = statusBarHeight + 8.dp, start = 16.dp, end = 16.dp)
            ) {
                SnackbarHost(hostState = snackbarHostState)
            }
        }
    }
}
