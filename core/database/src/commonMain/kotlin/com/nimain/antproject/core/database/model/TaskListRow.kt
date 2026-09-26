package com.nimain.antproject.core.database.model

import androidx.room.ColumnInfo
import kotlin.uuid.Uuid

data class TaskListRow(
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "due_date") val dueDate: String?,
    @ColumnInfo(name = "due_time") val dueTime: String?,
    @ColumnInfo(name = "is_recurring") val isRecurring: Boolean,
    @ColumnInfo(name = "project_title") val projectTitle: String?,
    @ColumnInfo(name = "total_subtasks") val totalSubtasks: Int,
    @ColumnInfo(name = "done_subtasks") val doneSubtasks: Int,
)
