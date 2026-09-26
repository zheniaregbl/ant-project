package com.nimain.antproject.core.domain.projects

/**
 * Строка списка проектов.
 *
 * @property activeTaskCount количество активных задач проекта.
 */
data class ProjectListItem(
    val id: ProjectId,
    val title: String,
    val activeTaskCount: Int,
)
