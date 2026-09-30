package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.domain.projects.ProjectId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class IdMappersTest {
    private val uuid = Uuid.generateV7()

    @Test
    fun taskIdSurvivesRoundTrip() {
        assertEquals(uuid, uuid.toTaskId().toUuid())
    }

    @Test
    fun subtaskIdSurvivesRoundTrip() {
        assertEquals(uuid, uuid.toSubtaskId().toUuid())
    }

    @Test
    fun projectIdParsesStandardUuidString() {
        assertEquals(uuid, ProjectId(uuid.toString()).toUuid())
    }

    @Test
    fun wrappedIdStringFails() {
        assertFailsWith<IllegalArgumentException> {
            ProjectId("ProjectId(value=$uuid)").toUuid()
        }
    }
}
