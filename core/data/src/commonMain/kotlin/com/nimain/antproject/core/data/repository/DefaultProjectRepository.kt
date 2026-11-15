package com.nimain.antproject.core.data.repository

import com.nimain.antproject.core.data.mapper.toItem
import com.nimain.antproject.core.database.dao.ProjectDao
import com.nimain.antproject.core.domain.projects.ProjectRepository
import com.nimain.antproject.core.domain.projects.model.Project
import com.nimain.antproject.core.domain.projects.model.ProjectId
import com.nimain.antproject.core.domain.projects.model.ProjectListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultProjectRepository(
    private val projectDao: ProjectDao,
) : ProjectRepository {
    override fun observeProjects(): Flow<List<ProjectListItem>> =
        projectDao.observeProjects().map { list -> list.map { it.toItem() } }

    override fun observeProject(id: ProjectId): Flow<Project?> {
        TODO("Not yet implemented")
    }

    override suspend fun createProject(title: String): ProjectId {
        TODO("Not yet implemented")
    }

    override suspend fun renameProject(
        id: ProjectId,
        title: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeProject(id: ProjectId) {
        TODO("Not yet implemented")
    }
}
