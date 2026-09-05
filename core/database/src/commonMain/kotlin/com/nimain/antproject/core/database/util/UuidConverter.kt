package com.nimain.antproject.core.database.util

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

internal class UuidConverter {
    @TypeConverter
    fun toBytes(uuid: Uuid): ByteArray = uuid.toByteArray()

    @TypeConverter
    fun fromBytes(bytes: ByteArray): Uuid = Uuid.fromByteArray(bytes)
}
