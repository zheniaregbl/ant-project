package com.nimain.antproject.core.designsystem.components.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme

private val CheckboxBorderWidth = 2.dp

@Composable
fun AntCheckbox(
    isDone: Boolean,
    color: Color,
    size: AntCheckboxSize,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxHeight()
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = null,
                ).padding(
                    start = AntTheme.spacing.screenHorizontal,
                    top = AntTheme.spacing.listItemVertical,
                    end = AntTheme.spacing.listItemVertical,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(size.size)
                        .background(
                            color = if (isDone) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape,
                        ).then(
                            if (!isDone) {
                                Modifier.border(
                                    width = CheckboxBorderWidth,
                                    color = color,
                                    shape = CircleShape,
                                )
                            } else {
                                Modifier
                            },
                        ),
            )
            if (isDone) {
                Icon(
                    modifier = Modifier.size(10.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
