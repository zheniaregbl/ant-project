package com.nimain.antproject.feature.tasks.inbox

import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.tasks.Priority
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import com.nimain.antproject.feature.tasks.fake.FakeTaskListRepository
import com.nimain.antproject.feature.tasks.fake.FakeTaskRepository
import com.nimain.antproject.tasks.inbox.InboxViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class)
class InboxViewModelTest {
    private val taskList = FakeTaskListRepository()
    private val tasks = FakeTaskRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun inboxTasksReachUiState() =
        runTest {
            val viewModel = InboxViewModel(taskList, tasks)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect {}
            }

            taskList.inbox.value = listOf(listItem("Купить молоко"))

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals(listOf("Купить молоко"), state.tasks.map { it.title })
        }

    @Test
    fun blankTitleCreatesNothing() =
        runTest {
            val viewModel = InboxViewModel(taskList, tasks)

            viewModel.onCreateTask("   ")

            assertTrue(tasks.createdDrafts.isEmpty())
        }

    @Test
    fun completingMissingTaskDoesNotCrash() =
        runTest {
            tasks.completeError = TaskNotFoundException(TaskId("missing"))
            val viewModel = InboxViewModel(taskList, tasks)

            viewModel.onCompleteTask(TaskId("missing"))
        }
}

@OptIn(ExperimentalUuidApi::class)
private fun listItem(title: String) =
    TaskListItem(
        id = TaskId(Uuid.generateV7().toString()),
        title = title,
        projectName = null,
        isRecurring = false,
        status = TaskListItemStatus.Active,
        priority = Priority.None,
        doneSubtasks = 0,
        totalSubtasks = 0,
        tags = emptyList(),
        dueDate = null,
        dueTime = null,
    )
