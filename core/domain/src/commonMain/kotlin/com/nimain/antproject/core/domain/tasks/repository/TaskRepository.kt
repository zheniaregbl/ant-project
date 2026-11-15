package com.nimain.antproject.core.domain.tasks.repository

import com.nimain.antproject.core.domain.projects.model.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tasks.model.Priority
import com.nimain.antproject.core.domain.tasks.model.SubtaskId
import com.nimain.antproject.core.domain.tasks.model.TaskDetail
import com.nimain.antproject.core.domain.tasks.model.TaskDraft
import com.nimain.antproject.core.domain.tasks.model.TaskId
import com.nimain.antproject.core.domain.tasks.result.CreateTaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

/**
 * Хранилище задач.
 *
 * Методы делятся на три вида:
 * - **Наблюдение** (`observe…`) — [Flow], который эмитит заново при каждом изменении данных.
 *   Живёт столько, сколько его собирает вызывающий, и отменяется вместе с ним.
 * - **Запись с результатом** ([createTask]) — `suspend`: вызывающий ждёт результат.
 *   Сама запись выполняется в скоупе уровня приложения и доводится до конца,
 *   даже если вызывающего отменили — отменяется только ожидание.
 * - **Запись «запустил и забыл»** — все остальные изменения. Возвращают управление сразу,
 *   запись выполняется в скоупе уровня приложения и переживает уход с экрана.
 *   Результат вызывающий видит через [observeTask]. Если задачи уже нет, изменение
 *   молча игнорируется; прочие ошибки записи только логируются и до вызывающего не доходят.
 *
 * Изменения задачи частичные: каждый метод обновляет только свои поля, поэтому
 * близкие по времени изменения разных полей не затирают друг друга.
 */
@Suppress("TooManyFunctions")
interface TaskRepository {
    /**
     * Наблюдает за задачей с [id] вместе с её подзадачами и тегами.
     *
     * Эмитит `null`, если задачи нет или её удалили.
     */
    fun observeTask(id: TaskId): Flow<TaskDetail?>

    /**
     * Создаёт новую задачу по данным из [draft] в проекте [projectId].
     * Если [projectId] равен `null`, задача попадает во «Входящие».
     *
     * @return [CreateTaskResult]
     */
    suspend fun createTask(
        draft: TaskDraft,
        projectId: ProjectId?,
    ): CreateTaskResult

    /**
     * Удаляет задачу вместе с её подзадачами и связями с тегами.
     *
     * Запустил и забыл: экран может закрыться сразу после вызова.
     * Если задачи с [id] нет, ничего не делает.
     */
    fun removeTask(id: TaskId)

    /**
     * Отмечает задачу выполненной: из активной или пропущенной — в выполненную.
     *
     * Запустил и забыл. Для уже выполненной задачи или если задачи с [id] нет, ничего не делает.
     *
     * @see reopenTask
     */
    fun completeTask(id: TaskId)

    /**
     * Возвращает выполненную задачу в активные.
     *
     * Пропущенная задача после отмены выполнения становится активной, а не пропущенной.
     * Запустил и забыл. Для невыполненной задачи или если задачи с [id] нет, ничего не делает.
     *
     * @see completeTask
     */
    fun reopenTask(id: TaskId)

    /**
     * Перемещает задачу с [id] в проект с [projectId].
     * Если [projectId] равен `null`, перемещает задачу во «Входящие».
     *
     * Запустил и забыл. Если задачи с [id] нет, ничего не делает.
     */
    fun moveTaskToProject(
        id: TaskId,
        projectId: ProjectId?,
    )

    /**
     * Обновляет заголовок задачи.
     *
     * Не проверяет [title] на пустоту: пустой заголовок недопустим, и вызывающий
     * не должен его передавать. Запустил и забыл. Если задачи с [id] нет, ничего не делает.
     */
    fun updateTitle(
        id: TaskId,
        title: String,
    )

    /**
     * Обновляет описание задачи. `null` — описания нет.
     *
     * Пустое описание вызывающий передаёт как `null`, а не как пустую строку.
     * Запустил и забыл. Если задачи с [id] нет, ничего не делает.
     */
    fun updateDescription(
        id: TaskId,
        description: String?,
    )

    /**
     * Обновляет приоритет задачи.
     *
     * Запустил и забыл. Если задачи с [id] нет, ничего не делает.
     */
    fun updatePriority(
        id: TaskId,
        priority: Priority,
    )

    /**
     * Обновляет срок задачи: дату и время одной записью.
     *
     * Время без даты недопустимо: если [dueDate] равен `null`, [dueTime] отбрасывается
     * и срок очищается целиком. Запустил и забыл. Если задачи с [id] нет, ничего не делает.
     */
    fun updateDueDateTime(
        id: TaskId,
        dueDate: LocalDate?,
        dueTime: LocalTime?,
    )

    /**
     * Добавляет в задачу [id] подзадачу с заголовком [title] в конец списка.
     *
     * Не проверяет [title] на пустоту: пустой заголовок недопустим, и вызывающий
     * не должен его передавать. Запустил и забыл. Если задачи с [id] нет, ничего не делает.
     */
    fun addSubtask(
        id: TaskId,
        title: String,
    )

    /**
     * Меняет заголовок подзадачи [subtaskId] в задаче [id].
     *
     * Не проверяет [title] на пустоту: пустой заголовок недопустим, и вызывающий
     * не должен его передавать. Запустил и забыл. Если подзадачи нет, ничего не делает.
     */
    fun renameSubtask(
        id: TaskId,
        subtaskId: SubtaskId,
        title: String,
    )

    /**
     * Отмечает подзадачу [subtaskId] в задаче [id] выполненной или снимает отметку.
     *
     * Идемпотентен: повторный вызов с тем же [isDone] ничего не меняет.
     * Запустил и забыл. Если подзадачи нет, ничего не делает.
     */
    fun setSubtaskDone(
        id: TaskId,
        subtaskId: SubtaskId,
        isDone: Boolean,
    )

    /**
     * Удаляет подзадачу [subtaskId] из задачи [id].
     *
     * Запустил и забыл. Если подзадачи нет, ничего не делает.
     */
    fun removeSubtask(
        id: TaskId,
        subtaskId: SubtaskId,
    )

    /**
     * Переключает тег [tagId] у задачи [taskId]: снимает, если он привязан, иначе привязывает.
     *
     * Решение принимается по текущему состоянию в базе, поэтому быстрые повторные
     * нажатия не теряются. Запустил и забыл. Если задачи или тега нет, ничего не делает.
     */
    fun toggleTag(
        id: TaskId,
        tagId: TagId,
    )
}
