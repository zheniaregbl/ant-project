package com.nimain.antproject.core.database.model

import androidx.room.ColumnInfo
import kotlin.uuid.Uuid

data class ProjectListRow(
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "active_task_count") val activeTaskCount: Int,
)
