package com.anasini.nuvodo.domain.usecase

import com.anasini.nuvodo.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteTask(id)
    }
}
