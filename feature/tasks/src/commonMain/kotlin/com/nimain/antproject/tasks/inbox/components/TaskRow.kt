package com.nimain.antproject.tasks.inbox.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus

@Composable
internal fun TaskRow(
    item: TaskListItem,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.status == TaskListItemStatus.Done,
            onCheckedChange = { onComplete() },
        )
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            taskMeta(item)?.let { meta ->
                Text(
                    text = meta,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Вторая строка: проект · срок · прогресс подзадач. `null`, если показать нечего. */
private fun taskMeta(item: TaskListItem): String? {
    val due =
        item.dueDate?.let { date ->
            item.dueTime?.let { time -> "$date $time" } ?: date.toString()
        }
    val subtasks = if (item.totalSubtasks > 0) "${item.doneSubtasks}/${item.totalSubtasks}" else null

    return listOfNotNull(item.projectName, due, subtasks)
        .joinToString(" · ")
        .ifEmpty { null }
}
