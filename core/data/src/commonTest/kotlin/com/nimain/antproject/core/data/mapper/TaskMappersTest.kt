package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.model.TaskListRow
import com.nimain.antproject.core.database.util.TaskStatusCode
import com.nimain.antproject.core.domain.tasks.Priority
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
private val ROW_ID = Uuid.parse("01a0ee83-4f57-705f-bc11-68afd1082e30")

@OptIn(ExperimentalUuidApi::class)
private val PROJECT_ID = Uuid.parse("01a0ee83-4f57-705f-bc11-68afd1082e31")

private val NOW = Instant.parse("2026-09-26T10:00:00Z")

@OptIn(ExperimentalUuidApi::class)
@Suppress("LongParameterList")
private fun row(
    status: String = TaskStatusCode.ACTIVE,
    priority: Int = Priority.None.weight,
    dueDate: String? = null,
    dueTime: String? = null,
    isRecurring: Boolean = false,
    projectTitle: String? = null,
    totalSubtasks: Int = 0,
    doneSubtasks: Int = 0,
) = TaskListRow(
    id = ROW_ID,
    title = "Задача",
    status = status,
    priority = priority,
    dueDate = dueDate,
    dueTime = dueTime,
    isRecurring = isRecurring,
    projectTitle = projectTitle,
    totalSubtasks = totalSubtasks,
    doneSubtasks = doneSubtasks,
)

@OptIn(ExperimentalUuidApi::class)
class TaskMappersTest {
    @Test
    fun rowStatusCodesMapToListItemStatuses() {
        val expected =
            mapOf(
                TaskStatusCode.ACTIVE to TaskListItemStatus.Active,
                TaskStatusCode.DONE to TaskListItemStatus.Done,
                TaskStatusCode.SKIPPED to TaskListItemStatus.Skipped,
            )

        expected.forEach { (code, status) ->
            assertEquals(status, row(status = code).toListItem().status, "code '$code'")
        }
    }

    @Test
    fun unknownStatusCodeFails() {
        val error =
            assertFailsWith<IllegalStateException> {
                row(status = "archived").toListItem()
            }

        assertContains(error.message.orEmpty(), "archived")
    }

    @Test
    fun everyPriorityWeightMapsBack() {
        Priority.entries.forEach { priority ->
            assertEquals(priority, row(priority = priority.weight).toListItem().priority, "priority $priority")
        }
    }

    @Test
    fun unknownPriorityWeightFails() {
        val error =
            assertFailsWith<IllegalStateException> {
                row(priority = 99).toListItem()
            }

        assertContains(error.message.orEmpty(), "99")
    }

    @Test
    fun rowFieldsAreCopiedToListItem() {
        val item =
            row(
                isRecurring = true,
                projectTitle = "Дом",
                totalSubtasks = 3,
                doneSubtasks = 2,
            ).toListItem()

        assertEquals(TaskId(ROW_ID.toString()), item.id)
        assertEquals("Задача", item.title)
        assertEquals("Дом", item.projectName)
        assertTrue(item.isRecurring)
        assertEquals(3, item.totalSubtasks)
        assertEquals(2, item.doneSubtasks)
    }

    @Test
    fun isoDueDateAndTimeAreParsed() {
        val item = row(dueDate = "2026-12-01", dueTime = "08:30").toListItem()

        assertEquals(LocalDate(2026, 12, 1), item.dueDate)
        assertEquals(LocalTime(8, 30), item.dueTime)
    }

    @Test
    fun missingDueDateTimeAndProjectMapToNull() {
        val item = row().toListItem()

        assertNull(item.dueDate)
        assertNull(item.dueTime)
        assertNull(item.projectName)
    }

    @Test
    fun malformedDueDateFails() {
        assertFailsWith<IllegalArgumentException> {
            row(dueDate = "2026-13-45").toListItem()
        }
    }

    @Test
    fun fullDraftMapsToEntity() {
        val draft =
            TaskDraft(
                title = "  Задача  ",
                description = "  Описание  ",
                priority = Priority.High,
                dueDate = LocalDate(2026, 12, 1),
                dueTime = LocalTime(8, 30),
            )

        val entity = draft.toEntity(id = ROW_ID, projectId = PROJECT_ID, position = 1.0, now = NOW)

        assertEquals(
            TaskEntity(
                id = ROW_ID,
                projectId = PROJECT_ID,
                title = "Задача",
                description = "Описание",
                status = TaskStatusCode.ACTIVE,
                priority = Priority.High.weight,
                position = 1.0,
                dueDate = "2026-12-01",
                dueTime = "08:30",
                recurrence = null,
                createdAt = NOW.toEpochMilliseconds(),
                updatedAt = NOW.toEpochMilliseconds(),
                completedAt = null,
                serverVersion = null,
                isDirty = true,
            ),
            entity,
        )
    }

    @Test
    fun minimalDraftMapsOptionalFieldsToNull() {
        val entity =
            TaskDraft(title = "Задача")
                .toEntity(id = ROW_ID, projectId = null, position = 1.0, now = NOW)

        assertNull(entity.projectId)
        assertNull(entity.description)
        assertNull(entity.dueDate)
        assertNull(entity.dueTime)
        assertEquals(Priority.None.weight, entity.priority)
    }
}
