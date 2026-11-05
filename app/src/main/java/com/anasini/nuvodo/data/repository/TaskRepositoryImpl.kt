package com.anasini.nuvodo.data.repository

import com.anasini.nuvodo.data.local.TaskDao
import com.anasini.nuvodo.data.local.TaskEntity
import com.anasini.nuvodo.domain.model.Task
import com.anasini.nuvodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    companion object {
        const val MAX_ACTIVE_TASKS = 6
    }

    override fun getActiveTasks(): Flow<List<Task>> {
        return taskDao.getActiveTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getActiveTaskCount(): Int {
        return taskDao.getActiveTaskCount()
    }

    override fun getArchivedTasks(): Flow<List<Task>> {
        return taskDao.getArchivedTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addTask(title: String): Result<Long> {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("Task title cannot be empty"))
        }

        val activeCount = taskDao.getActiveTaskCount()
        if (activeCount >= MAX_ACTIVE_TASKS) {
            return Result.failure(IllegalStateException("Maximum of $MAX_ACTIVE_TASKS active tasks reached"))
        }

        val entity = TaskEntity(
            title = trimmed,
            createdAt = System.currentTimeMillis(),
            isCompleted = false,
            isArchived = false
        )
        val id = taskDao.insertTask(entity)
        return Result.success(id)
    }

    override suspend fun completeTask(id: Long) {
        taskDao.markTaskCompleted(id, System.currentTimeMillis())
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTaskById(id)
    }

    override suspend fun autoArchivePreviousDayTasks(): Int {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val cutoffTimestamp = calendar.timeInMillis
        val now = System.currentTimeMillis()
        return taskDao.autoArchiveExpiredTasks(cutoffTimestamp, now)
    }

    private fun TaskEntity.toDomain(): Task {
        return Task(
            id = id,
            title = title,
            createdAt = createdAt,
            isCompleted = isCompleted,
            completedAt = completedAt,
            isArchived = isArchived,
            archivedAt = archivedAt
        )
    }
}
