package com.nimain.antproject.core.designsystem.components.taskrow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nimain.antproject.core.designsystem.theme.AntTheme

private val ProjectTitleMaxWidth = 140.dp
private val CheckboxCircleSize = 20.dp
private val CheckboxBorderWidth = 2.dp
private val MetaSectionTopPadding = 6.dp

@Composable
fun TaskRow(
    model: TaskRowModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                ),
    ) {
        CircleCheckboxSection(
            isDone = model.isDone,
            color = model.priority.color(),
            onClick = onCheckedChange,
        )
        Column(modifier = Modifier.weight(1f)) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            end = AntTheme.spacing.screenHorizontal,
                            bottom = AntTheme.spacing.listItemVertical,
                        ),
            ) {
                TaskTitle(title = model.title, isDone = model.isDone)
                if (model.hasMetadata) MetaRow(model = model)
            }
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = MaterialTheme.colorScheme.outlineVariant),
            )
        }
    }
}

@Composable
private fun TaskTitle(
    modifier: Modifier = Modifier,
    title: String,
    isDone: Boolean,
) {
    Text(
        modifier = modifier.padding(top = AntTheme.spacing.listItemVertical),
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        color = if (isDone) AntTheme.colors.textFaint else MaterialTheme.colorScheme.onSurface,
        textDecoration =
            if (isDone) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            },
    )
}

@Composable
private fun MetaRow(
    modifier: Modifier = Modifier,
    model: TaskRowModel,
) {
    FlowRow(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = MetaSectionTopPadding),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        if (model.subtasks != null) {
            Text(
                text = "✓ ${model.subtasks.done}/${model.subtasks.total}",
                style = AntTheme.typography.meta,
                color = AntTheme.colors.textMuted,
            )
        }
        if (model.due != null) {
            Text(
                text = model.due.text,
                style = AntTheme.typography.meta,
                color = model.due.status.color(),
            )
        }
        model.tags.forEach {
            Text(
                text = it,
                style = AntTheme.typography.meta,
                color = AntTheme.colors.textMuted,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (model.projectTitle != null) {
            Text(
                modifier = Modifier.widthIn(max = ProjectTitleMaxWidth),
                text = model.projectTitle,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.5.sp,
                        lineHeight = 16.sp,
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = AntTheme.colors.textMuted,
            )
        }
    }
}

@Composable
private fun CircleCheckboxSection(
    isDone: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxHeight()
                .clickable(onClick = onClick)
                .padding(
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
                        .size(CheckboxCircleSize)
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

private val TaskRowModel.hasMetadata: Boolean
    get() = subtasks != null || due != null || tags.isNotEmpty() || projectTitle != null

@Composable
private fun TaskRowPriority.color(): Color =
    when (this) {
        TaskRowPriority.None -> AntTheme.colors.priorityNone
        TaskRowPriority.Low -> AntTheme.colors.priorityLow
        TaskRowPriority.Medium -> AntTheme.colors.priorityMedium
        TaskRowPriority.High -> AntTheme.colors.priorityHigh
    }

@Composable
private fun TaskRowDueStatus.color(): Color =
    when (this) {
        TaskRowDueStatus.Overdue -> AntTheme.colors.overdue
        TaskRowDueStatus.Today -> MaterialTheme.colorScheme.primary
        TaskRowDueStatus.Upcoming -> AntTheme.colors.textMuted
    }

@Suppress("LongMethod")
@Composable
private fun TaskRowCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            TaskRow(
                model = TaskRowModel(title = "Простая задача"),
                onClick = {},
                onLongClick = {},
                onCheckedChange = {},
            )
            TaskRow(
                model =
                    TaskRowModel(
                        title = "Задача проекта",
                        projectTitle = "Проект 1",
                    ),
                onClick = {},
                onLongClick = {},
                onCheckedChange = {},
            )
            TaskRow(
                model =
                    TaskRowModel(
                        title = "Задача проекта",
                        projectTitle = "Длинное название какого-то проекта",
                    ),
                onClick = {},
                onLongClick = {},
                onCheckedChange = {},
            )
            TaskRow(
                model =
                    TaskRowModel(
                        title = "Выполненная задача",
                        isDone = true,
                    ),
                onClick = {},
                onLongClick = {},
                onCheckedChange = {},
            )
            TaskRow(
                model =
                    TaskRowModel(
                        title = "Срочная задача",
                        priority = TaskRowPriority.High,
                    ),
                onClick = {},
                onLongClick = {},
                onCheckedChange = {},
            )
            TaskRow(
                model =
                    TaskRowModel(
                        title = "Большая задача",
                        subtasks =
                            TaskRowSubtasks(
                                done = 2,
                                total = 5,
                            ),
                    ),
                onClick = {},
                onLongClick = {},
                onCheckedChange = {},
            )
            TaskRow(
                model =
                    TaskRowModel(
                        title = "Задача 1",
                        projectTitle = "Проект 1",
                        isDone = false,
                        due =
                            TaskRowDue(
                                text = "вчера",
                                status = TaskRowDueStatus.Overdue,
                            ),
                        priority = TaskRowPriority.Low,
                        tags = listOf("#Тег 1", "#Тег 2", "#Тег 3"),
                        subtasks =
                            TaskRowSubtasks(
                                done = 2,
                                total = 5,
                            ),
                    ),
                onClick = {},
                onLongClick = {},
                onCheckedChange = {},
            )
        }
    }
}

@Preview
@Composable
private fun TaskRowLightPreview() {
    AntTheme(darkTheme = false) { TaskRowCatalog() }
}

@Preview
@Composable
private fun TaskRowDarkPreview() {
    AntTheme(darkTheme = true) { TaskRowCatalog() }
}
