package com.nimain.antproject.core.domain.tasks

import com.nimain.antproject.core.domain.projects.ProjectId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskListRepository {
    // Inbox

    /**
     * Наблюдает за активными задачами, не связанными с проектами.
     * Новые задачи идут сверху.
     */
    fun observeInboxTasks(): Flow<List<TaskListItem>>

    // Project tasks

    /**
     * Наблюдает за активными задачами проекта с [projectId].
     * Сначала идут задачи со сроком по возрастанию даты,
     * затем задачи без срока, новые сверху.
     */
    fun observeProjectTasks(projectId: ProjectId): Flow<List<TaskListItem>>

    // Today

    /**
     * Наблюдает за активными задачами на [today] и просроченными активными задачами прошлых дней.
     * Задачи без срока в выборку не попадают, как и задачи в статусах,
     * отличных от [TaskStatus.Active].
     *
     * Порядок: сначала задачи прошлых дней по возрастанию даты,
     * затем задачи на [today] без времени, затем задачи на [today] со временем
     * по возрастанию времени.
     */
    fun observeTodayTasks(today: LocalDate): Flow<List<TaskListItem>>

    // Date range

    /**
     * Наблюдает за активными задачами со сроком с [fromDate] по [toDate] включительно.
     * Задачи без срока в выборку не попадают.
     *
     * Порядок: по возрастанию даты, внутри дня сначала задачи без времени,
     * затем со временем по возрастанию времени.
     */
    fun observeDateRangeTasks(
        fromDate: LocalDate,
        toDate: LocalDate,
    ): Flow<List<TaskListItem>>
}
