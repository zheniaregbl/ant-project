package com.nimain.antproject.feature.tasks.inbox

import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.tasks.Priority
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import com.nimain.antproject.feature.tasks.fake.FakeTaskListRepository
import com.nimain.antproject.feature.tasks.fake.FakeTaskRepository
import com.nimain.antproject.tasks.inbox.InboxAction
import com.nimain.antproject.tasks.inbox.InboxUiState
import com.nimain.antproject.tasks.inbox.InboxViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
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

    private fun TestScope.createViewModel(): InboxViewModel {
        val viewModel = InboxViewModel(taskList, tasks)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        return viewModel
    }

    private fun InboxViewModel.content(): List<TaskListItem> = assertIs<InboxUiState.Content>(uiState.value).tasks

    private fun TestScope.waitForCompletedTaskRemoval() {
        advanceTimeBy(1_000.milliseconds)
        runCurrent()
    }

    @Test
    fun loadingUntilRepositoryEmits() =
        runTest {
            val viewModel = createViewModel()

            assertIs<InboxUiState.Loading>(viewModel.uiState.value)
        }

    @Test
    fun emptyInboxGivesEmptyState() =
        runTest {
            val viewModel = createViewModel()

            taskList.emitInbox(emptyList())

            assertIs<InboxUiState.Empty>(viewModel.uiState.value)
        }

    @Test
    fun inboxTasksReachUiState() =
        runTest {
            val viewModel = createViewModel()

            taskList.emitInbox(listOf(listItem("Купить молоко")))

            assertEquals(listOf("Купить молоко"), viewModel.content().map { it.title })
        }

    @Test
    fun failureGivesErrorState() =
        runTest {
            val viewModel = createViewModel()

            taskList.emitInboxFailure()

            assertIs<InboxUiState.Error>(viewModel.uiState.value)
        }

    @Test
    fun checkedTaskIsCompletedInRepository() =
        runTest {
            val task = listItem("A")
            val viewModel = createViewModel()
            taskList.emitInbox(listOf(task))

            viewModel.onAction(InboxAction.TaskChecked(task.id))

            assertEquals(listOf(task.id), tasks.completedIds)
        }

    @Test
    fun checkedTaskStaysInPlaceAsDoneThenDisappears() =
        runTest {
            val a = listItem("A")
            val b = listItem("B")
            val c = listItem("C")
            val viewModel = createViewModel()
            taskList.emitInbox(listOf(a, b, c))

            viewModel.onAction(InboxAction.TaskChecked(b.id))
            taskList.emitInbox(listOf(a, c))

            val during = viewModel.content()
            assertEquals(listOf(a.id, b.id, c.id), during.map { it.id })
            assertEquals(TaskListItemStatus.Done, during[1].status)

            waitForCompletedTaskRemoval()

            assertEquals(listOf(a.id, c.id), viewModel.content().map { it.id })
        }

    @Test
    fun lastCheckedTaskKeepsContentUntilDelayEnds() =
        runTest {
            val task = listItem("A")
            val viewModel = createViewModel()
            taskList.emitInbox(listOf(task))

            viewModel.onAction(InboxAction.TaskChecked(task.id))
            taskList.emitInbox(emptyList())

            assertEquals(listOf(task.id), viewModel.content().map { it.id })

            waitForCompletedTaskRemoval()

            assertIs<InboxUiState.Empty>(viewModel.uiState.value)
        }

    @Test
    fun repeatedCheckCompletesTaskOnce() =
        runTest {
            val task = listItem("A")
            val viewModel = createViewModel()
            taskList.emitInbox(listOf(task))

            viewModel.onAction(InboxAction.TaskChecked(task.id))
            viewModel.onAction(InboxAction.TaskChecked(task.id))

            assertEquals(listOf(task.id), tasks.completedIds)
        }

    @Test
    fun checkingUnknownTaskDoesNothing() =
        runTest {
            val viewModel = createViewModel()
            taskList.emitInbox(listOf(listItem("A")))

            viewModel.onAction(InboxAction.TaskChecked(TaskId("unknown")))

            assertTrue(tasks.completedIds.isEmpty())
        }

    @Test
    fun missingTaskRollsBackDoneSnapshot() =
        runTest {
            val task = listItem("A")
            tasks.completeError = TaskNotFoundException(task.id)
            val viewModel = createViewModel()
            taskList.emitInbox(listOf(task))

            viewModel.onAction(InboxAction.TaskChecked(task.id))

            assertEquals(TaskListItemStatus.Active, viewModel.content().single().status)
        }

    @Test
    fun confirmedDeleteRemovesTask() =
        runTest {
            val task = listItem("A")
            val viewModel = createViewModel()
            taskList.emitInbox(listOf(task))

            viewModel.onAction(InboxAction.DeleteTaskConfirmed(task.id))

            assertEquals(listOf(task.id), tasks.removedIds)
        }
}

@OptIn(ExperimentalUuidApi::class)
private fun listItem(
    title: String,
    id: TaskId = TaskId(Uuid.generateV7().toString()),
) = TaskListItem(
    id = id,
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
