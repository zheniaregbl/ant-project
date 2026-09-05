package com.nimain.antproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "outbox",
    primaryKeys = ["entity_type", "entity_id"],
)
internal data class OutboxEntity(
    @ColumnInfo(name = "entity_type")
    val entityType: String,
    @ColumnInfo(name = "entity_id")
    val entityId: Uuid,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long,
)
