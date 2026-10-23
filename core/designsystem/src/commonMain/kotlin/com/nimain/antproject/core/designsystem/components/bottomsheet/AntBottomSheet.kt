package com.nimain.antproject.core.designsystem.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme
import kotlinx.coroutines.launch

private val DragHandleWidth = 36.dp
private val DragHandleHeight = 5.dp
private val DragHandleTopPadding = 10.dp
private val DragHandleBottomPadding = 6.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
) {
    val sheetState =
        rememberBottomSheetState(
            initialValue = SheetValue.Hidden,
            enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
        )
    val scope = rememberCoroutineScope()
    val currentOnDismissRequest by rememberUpdatedState(onDismissRequest)

    val dismiss: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) currentOnDismissRequest()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = sheetShape(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        scrimColor = AntTheme.colors.scrim,
        dragHandle = { AntSheetDragHandle() },
    ) {
        content(dismiss)
    }
}

@Composable
private fun sheetShape(): Shape =
    MaterialTheme.shapes.extraLarge.copy(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
    )

@Composable
private fun AntSheetDragHandle() {
    Box(
        modifier =
            Modifier
                .padding(top = DragHandleTopPadding, bottom = DragHandleBottomPadding)
                .size(width = DragHandleWidth, height = DragHandleHeight)
                .background(MaterialTheme.colorScheme.outline, CircleShape),
    )
}

@Composable
internal fun AntBottomSheetPreviewContainer(content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = sheetShape(),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AntSheetDragHandle()
            Column(modifier = Modifier.fillMaxWidth()) {
                content {}
            }
        }
    }
}

@Composable
private fun AntBottomSheetCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier =
                Modifier
                    .background(AntTheme.colors.scrim)
                    .padding(top = 120.dp),
        ) {
            AntBottomSheetPreviewContainer { dismiss ->
                Text(
                    text = "Оплатить хостинг",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier =
                        Modifier.padding(
                            horizontal = AntTheme.spacing.screenHorizontal,
                            vertical = 12.dp,
                        ),
                )
                AntSheetItem(text = "Дублировать", onClick = dismiss)
                AntSheetItem(text = "Удалить", onClick = dismiss, destructive = true)
            }
        }
    }
}

@Preview
@Composable
private fun AntBottomSheetLightPreview() {
    AntTheme(darkTheme = false) { AntBottomSheetCatalog() }
}

@Preview
@Composable
private fun AntBottomSheetDarkPreview() {
    AntTheme(darkTheme = true) { AntBottomSheetCatalog() }
}
