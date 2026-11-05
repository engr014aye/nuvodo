package com.anasini.nuvodo.domain.usecase

import com.anasini.nuvodo.domain.model.Task
import com.anasini.nuvodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetArchivedTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.getArchivedTasks()
}
