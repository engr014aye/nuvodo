package com.anasini.nuvodo.domain.usecase

import com.anasini.nuvodo.domain.model.Task
import com.anasini.nuvodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetActiveTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.getActiveTasks()
}
