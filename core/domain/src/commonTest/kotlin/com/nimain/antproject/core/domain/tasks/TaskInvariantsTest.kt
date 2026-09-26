package com.nimain.antproject.core.domain.tasks

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TaskInvariantsTest {
    @Test
    fun isDoneIsTrueWhenStatusIsDone() {
        val task = task(status = TaskStatus.Done(now))
        assertTrue(task.isDone)
    }

    @Test
    fun isDoneIsFalseFalseWhenStatusIsActive() {
        val task = task()
        assertFalse(task.isDone)
    }

    @Test
    fun isDoneReturnFalseWhenSkipped() {
        val task = task(status = TaskStatus.Skipped, seriesId = SeriesId("series-1"))
        assertFalse(task.isDone)
    }

    @Test
    fun skippedTaskIsRejectedWhenItHasNoSeries() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                task(status = TaskStatus.Skipped)
            }
        assertContains(error.message.orEmpty(), "series")
    }

    @Test
    fun skippedTaskIsAllowedWhenItBelongsToSeries() {
        val task = task(status = TaskStatus.Skipped, seriesId = SeriesId("series-1"))
        assertEquals(TaskStatus.Skipped, task.status)
    }

    @Test
    fun taskWithRecurrenceIsRejectedWhenStatusIsDone() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                task(status = TaskStatus.Done(now), recurrence = Recurrence.Daily(), seriesId = SeriesId("series-1"))
            }
        assertContains(error.message.orEmpty(), "only active tasks")
    }

    @Test
    fun taskWithRecurrenceIsRejectedWhenStatusIsSkipped() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                task(status = TaskStatus.Skipped, recurrence = Recurrence.Daily(), seriesId = SeriesId("series-1"))
            }
        assertContains(error.message.orEmpty(), "only active tasks")
    }

    @Test
    fun activeTaskWithSeriesIdIsAllowedWhenUserDeletedRecurrence() {
        val task = task(seriesId = SeriesId("series-1"))
        assertEquals(TaskStatus.Active, task.status)
    }

    @Test
    fun taskWithRecurrenceIsRejectedWhenStatusChangedToDone() {
        var task = task(seriesId = SeriesId("series-1"), recurrence = Recurrence.Daily())
        val error =
            assertFailsWith<IllegalArgumentException> {
                task = task.copy(status = TaskStatus.Done(now))
            }
        assertContains(error.message.orEmpty(), "only active tasks")

        task = task.copy(status = TaskStatus.Done(now), recurrence = null)
        assertNull(task.recurrence)
    }

    @Test
    fun taskIsRejectedWhenTitleIsBlank() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                task(title = "    ")
            }
        assertContains(error.message.orEmpty(), "blank")
    }

    @Test
    fun taskIsRejectedWhenDescriptionIsBlank() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                task(description = "")
            }
        assertContains(error.message.orEmpty(), "blank")
    }

    @Test
    fun taskIsAllowedWhenDescriptionIsNull() {
        val task = task()
        assertNull(task.description)
    }

    @Test
    fun taskIsAllowedWhenDueDateAndTimeAreNull() {
        val task = task()
        assertNull(task.dueDate)
        assertNull(task.dueTime)
    }

    @Test
    fun taskIsAllowedWhenDueDateAndTimeAreNotNull() {
        val task = task(dueDate = LocalDate(2026, 1, 1), dueTime = LocalTime(1, 1))
        assertNotNull(task.dueDate)
        assertNotNull(task.dueTime)
    }

    @Test
    fun taskIsRejectedWhenDueTimeIsNotAndDateIsNull() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                task(dueTime = LocalTime(0, 0))
            }
        assertContains(error.message.orEmpty(), "have not dueDate")
    }

    @Test
    fun taskIsRejectedWhenDueDateLaterThanRecurrenceUntil() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                task(
                    seriesId = SeriesId("series-1"),
                    dueDate = LocalDate(2026, 11, 1),
                    recurrence = Recurrence.Daily(until = LocalDate(2026, 10, 1)),
                )
            }
        assertContains(error.message.orEmpty(), "later than until field")
    }

    @Test
    fun taskIsAllowedWhenDueDateEqualRecurrenceUntil() {
        val task =
            task(
                seriesId = SeriesId("series-1"),
                dueDate = LocalDate(2026, 10, 1),
                recurrence = Recurrence.Daily(until = LocalDate(2026, 10, 1)),
            )
        assertEquals(task.dueDate, task.recurrence!!.until)
    }

    @Test
    fun subtaskIsRejectedWhenTitleIsBlank() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                sub(title = "")
            }
        assertContains(error.message.orEmpty(), "blank")
    }
}
