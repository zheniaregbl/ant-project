package com.nimain.antproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "subtask",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["task_id", "position"])],
)
internal data class SubtaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Uuid,
    @ColumnInfo(name = "task_id")
    val taskId: Uuid,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean,
    @ColumnInfo(name = "position")
    val position: Double,
)
