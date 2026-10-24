package com.nimain.antproject.core.designsystem.components.layout.loading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.components.layout.StateLayout
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private val DefaultShowDelay = 400.milliseconds
private val IndicatorSize = 32.dp
private val IndicatorStrokeWidth = 3.dp

@Composable
fun LoadingStateLayout(
    modifier: Modifier = Modifier,
    showDelay: Duration = DefaultShowDelay,
) {
    var isIndicatorVisible by remember(showDelay) {
        mutableStateOf(showDelay <= Duration.ZERO)
    }

    LaunchedEffect(showDelay) {
        if (!isIndicatorVisible) {
            delay(showDelay)
            isIndicatorVisible = true
        }
    }

    StateLayout(
        modifier = modifier,
        visual = {
            AnimatedVisibility(
                visible = isIndicatorVisible,
                enter = fadeIn(),
                exit = ExitTransition.None,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(IndicatorSize),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = IndicatorStrokeWidth,
                )
            }
        },
    )
}
