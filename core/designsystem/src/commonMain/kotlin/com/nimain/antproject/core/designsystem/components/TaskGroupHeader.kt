package com.nimain.antproject.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRow
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowDue
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowDueStatus
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowModel
import com.nimain.antproject.core.designsystem.theme.AntTheme

private val HeaderTopPadding = 18.dp
private val HeaderBottomPadding = 8.dp
private val HeaderItemsGap = 8.dp
private val ChevronSize = 10.dp
private val ChevronStrokeWidth = 1.5.dp
private const val COLLAPSED_CHEVRON_ROTATION = -90f

enum class TaskGroupHeaderStyle {
    Default,
    Overdue,
}

@Composable
fun TaskGroupHeader(
    title: String,
    count: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    style: TaskGroupHeaderStyle = TaskGroupHeaderStyle.Default,
) {
    val color =
        when (style) {
            TaskGroupHeaderStyle.Default -> AntTheme.colors.textMuted
            TaskGroupHeaderStyle.Overdue -> AntTheme.colors.overdue
        }
    val textStyle =
        when (style) {
            TaskGroupHeaderStyle.Default -> AntTheme.typography.sectionLabel
            TaskGroupHeaderStyle.Overdue ->
                AntTheme.typography.sectionLabel.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.5.sp,
                    lineHeight = 10.5.sp,
                    letterSpacing = 0.12.em,
                )
        }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 0f else COLLAPSED_CHEVRON_ROTATION,
        label = "chevronRotation",
    )

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .semantics {
                    if (expanded) {
                        collapse {
                            onToggle()
                            true
                        }
                    } else {
                        expand {
                            onToggle()
                            true
                        }
                    }
                }.padding(
                    start = AntTheme.spacing.screenHorizontal,
                    end = AntTheme.spacing.screenHorizontal,
                    top = HeaderTopPadding,
                    bottom = HeaderBottomPadding,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HeaderItemsGap),
    ) {
        Text(text = title.uppercase(), style = textStyle, color = color)
        Text(text = count.toString(), style = textStyle, color = AntTheme.colors.textFaint)
        Spacer(modifier = Modifier.weight(1f))
        Chevron(
            color = color,
            modifier =
                Modifier
                    .size(ChevronSize)
                    .rotate(chevronRotation),
        )
    }
}

@Composable
private fun Chevron(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val path =
            Path().apply {
                moveTo(size.width * 0.2f, size.height * 0.35f)
                lineTo(size.width * 0.5f, size.height * 0.65f)
                lineTo(size.width * 0.8f, size.height * 0.35f)
            }
        drawPath(
            path = path,
            color = color,
            style =
                Stroke(
                    width = ChevronStrokeWidth.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
        )
    }
}

@Composable
private fun TaskGroupHeaderCatalog() {
    var overdueExpanded by remember { mutableStateOf(true) }
    var todayExpanded by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            TaskGroupHeader(
                title = "Просроченные",
                count = 3,
                expanded = overdueExpanded,
                onToggle = { overdueExpanded = !overdueExpanded },
                style = TaskGroupHeaderStyle.Overdue,
            )
            if (overdueExpanded) {
                TaskRow(
                    model =
                        TaskRowModel(
                            title = "Оплатить хостинг",
                            due = TaskRowDue(text = "вчера", status = TaskRowDueStatus.Overdue),
                        ),
                    onClick = {},
                    onLongClick = {},
                    onCheckedChange = {},
                )
            }
            TaskGroupHeader(
                title = "Без срока",
                count = 5,
                expanded = todayExpanded,
                onToggle = { todayExpanded = !todayExpanded },
            )
        }
    }
}

@Preview
@Composable
private fun TaskGroupHeaderLightPreview() {
    AntTheme(darkTheme = false) { TaskGroupHeaderCatalog() }
}

@Preview
@Composable
private fun TaskGroupHeaderDarkPreview() {
    AntTheme(darkTheme = true) { TaskGroupHeaderCatalog() }
}
