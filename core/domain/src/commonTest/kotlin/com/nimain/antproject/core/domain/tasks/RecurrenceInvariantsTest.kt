package com.nimain.antproject.core.domain.tasks

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RecurrenceInvariantsTest {
    @Test
    fun recurrenceIsRejectedWhenIntervalEqualsZero() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Recurrence.Daily(interval = 0)
            }
        assertContains(error.message.orEmpty(), "can not be less than 1")
    }

    @Test
    fun recurrenceIsRejectedWhenIntervalIsNegative() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Recurrence.Daily(interval = -1)
            }
        assertContains(error.message.orEmpty(), "can not be less than 1")
    }

    @Test
    fun recurrenceIsAllowedWhenIntervalIsPositive() {
        val recurrence = Recurrence.Daily()
        assertTrue(recurrence.interval > 0)
    }

    @Test
    fun recurrenceIsRejectedWhenHaveNotDaysOfWeek() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Recurrence.Weekly(daysOfWeek = setOf())
            }
        assertContains(error.message.orEmpty(), "daysOfWeek")
    }

    @Test
    fun recurrenceIsRejectedWhenDayOfMonthIsNegative() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Recurrence.Monthly(dayOfMonth = -1)
            }
        assertContains(error.message.orEmpty(), "dayOfMonth")
    }

    @Test
    fun recurrenceIsRejectedWhenDayOfMonthBelowThanOne() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Recurrence.Monthly(dayOfMonth = 0)
            }
        assertContains(error.message.orEmpty(), "dayOfMonth")
    }

    @Test
    fun recurrenceIsRejectedWhenDayOfMonthIsHigherThanThirtyOne() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Recurrence.Monthly(dayOfMonth = 32)
            }
        assertContains(error.message.orEmpty(), "dayOfMonth")
    }

    @Test
    fun recurrenceIsAllowedWhenDayOfMonthIsBetweenOneAndThirtyOne() {
        val recurrence: Recurrence.Monthly = Recurrence.Monthly(dayOfMonth = 28)
        assertTrue(recurrence.dayOfMonth in 1..31)
    }
}
