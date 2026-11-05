package com.anasini.nuvodo.domain.repository

import com.anasini.nuvodo.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getActiveTasks(): Flow<List<Task>>
    suspend fun getActiveTaskCount(): Int
    fun getArchivedTasks(): Flow<List<Task>>
    suspend fun addTask(title: String): Result<Long>
    suspend fun completeTask(id: Long)
    suspend fun deleteTask(id: Long)
    suspend fun autoArchivePreviousDayTasks(): Int
}
