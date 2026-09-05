package com.nimain.antproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(tableName = "project")
internal data class ProjectEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Uuid,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "server_version")
    val serverVersion: Long?,
    @ColumnInfo(name = "is_dirty")
    val isDirty: Boolean,
)
