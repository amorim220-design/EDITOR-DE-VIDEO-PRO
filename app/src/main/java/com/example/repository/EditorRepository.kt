package com.example.repository

import com.example.data.Clip
import com.example.data.ClipDao
import com.example.data.Project
import com.example.data.ProjectDao
import kotlinx.coroutines.flow.Flow

class EditorRepository(
    private val projectDao: ProjectDao,
    private val clipDao: ClipDao
) {
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    suspend fun createProject(name: String): Long {
        val project = Project(name = name)
        return projectDao.insertProject(project)
    }

    suspend fun getProjectById(id: Long): Project? = projectDao.getProjectById(id)

    fun getClipsForProject(projectId: Long): Flow<List<Clip>> = clipDao.getClipsForProject(projectId)

    suspend fun saveClips(clips: List<Clip>) {
        if (clips.isNotEmpty()) {
            clipDao.deleteClipsForProject(clips.first().projectId)
            clipDao.insertClips(clips)
        }
    }

    suspend fun deleteProject(project: Project) {
        clipDao.deleteClipsForProject(project.id)
        projectDao.deleteProject(project)
    }
}
