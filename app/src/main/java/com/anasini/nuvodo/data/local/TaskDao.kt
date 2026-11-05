package com.anasini.nuvodo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE isArchived = 0 ORDER BY createdAt ASC")
    fun getActiveTasks(): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE isArchived = 0")
    suspend fun getActiveTaskCount(): Int

    @Query("SELECT * FROM tasks WHERE isArchived = 1 ORDER BY coalesce(completedAt, archivedAt, createdAt) DESC")
    fun getArchivedTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    @Query("UPDATE tasks SET isCompleted = 1, completedAt = :completedAt, isArchived = 1 WHERE id = :id")
    suspend fun markTaskCompleted(id: Long, completedAt: Long)

    @Query("UPDATE tasks SET isArchived = 1, archivedAt = :archivedAt WHERE isArchived = 0 AND isCompleted = 0 AND createdAt < :cutoffTimestamp")
    suspend fun autoArchiveExpiredTasks(cutoffTimestamp: Long, archivedAt: Long): Int
}
