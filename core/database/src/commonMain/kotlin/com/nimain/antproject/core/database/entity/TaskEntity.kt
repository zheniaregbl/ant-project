package com.nimain.antproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["project_id", "position"]),
        Index(value = ["is_dirty"]),
        Index(value = ["next_occurrence_at"]),
    ],
)
internal data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Uuid,
    @ColumnInfo(name = "project_id")
    val projectId: Uuid? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "priority")
    val priority: Int,
    @ColumnInfo(name = "position")
    val position: Double,
    @ColumnInfo(name = "due_date")
    val dueDate: Long? = null,
    @ColumnInfo(name = "recurrence")
    val recurrence: String? = null,
    @ColumnInfo(name = "next_occurrence_at")
    val nextOccurrenceAt: Long? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "server_version")
    val serverVersion: Long? = null,
    @ColumnInfo(name = "is_dirty")
    val isDirty: Boolean,
)
