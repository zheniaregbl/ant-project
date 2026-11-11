package com.nimain.antproject.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Junction
import androidx.room.Relation
import com.nimain.antproject.core.database.entity.ProjectEntity
import com.nimain.antproject.core.database.entity.SubtaskEntity
import com.nimain.antproject.core.database.entity.TagEntity
import com.nimain.antproject.core.database.entity.TaskTagEntity
import kotlin.uuid.Uuid

data class TaskDetailRow(
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "due_date") val dueDate: String?,
    @ColumnInfo(name = "due_time") val dueTime: String?,
    @ColumnInfo(name = "project_id") val projectId: Uuid?,
    @Relation(parentColumn = "project_id", entityColumn = "id")
    val project: ProjectEntity?,
    @Relation(parentColumn = "id", entityColumn = "task_id")
    val subtasks: List<SubtaskEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy =
            Junction(
                value = TaskTagEntity::class,
                parentColumn = "task_id",
                entityColumn = "tag_id",
            ),
    )
    val tags: List<TagEntity>,
)
