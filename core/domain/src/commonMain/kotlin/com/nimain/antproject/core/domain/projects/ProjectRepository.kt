package com.nimain.antproject.core.domain.projects

import com.nimain.antproject.core.domain.exception.ProjectNotFoundException
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    /**
     * Наблюдает за всеми проектами.
     * Порядок: по алфавиту без учёта регистра.
     */
    fun observeProjects(): Flow<List<ProjectListItem>>

    /**
     * Наблюдает за проектом с [id].
     * Отдаёт `null`, если проекта нет или его удалили.
     */
    fun observeProject(id: ProjectId): Flow<Project?>

    /**
     * Создаёт проект с названием [title].
     * Названия проектов не обязаны быть уникальными.
     * Пустой [title] — ошибка вызывающего кода: ввод должен проверять экран.
     *
     * @return идентификатор созданного проекта.
     * @throws IllegalArgumentException если [title] пустой.
     */
    suspend fun createProject(title: String): ProjectId

    /**
     * Меняет название проекта с [id] на [title].
     * Пустой [title] — ошибка вызывающего кода: ввод должен проверять экран.
     *
     * @throws ProjectNotFoundException если проекта с [id] нет.
     * @throws IllegalArgumentException если [title] пустой.
     */
    suspend fun renameProject(
        id: ProjectId,
        title: String,
    )

    /**
     * Удаляет проект с [id] вместе со всеми его задачами,
     * включая выполненные, и их подзадачами.
     * Если проекта с [id] нет, ничего не делает.
     */
    suspend fun removeProject(id: ProjectId)
}
