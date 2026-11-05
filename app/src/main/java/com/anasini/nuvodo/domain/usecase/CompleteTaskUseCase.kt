package com.anasini.nuvodo.domain.usecase

import com.anasini.nuvodo.domain.repository.TaskRepository

class CompleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.completeTask(id)
    }
}
