package com.anasini.nuvodo.domain.usecase

import com.anasini.nuvodo.domain.repository.TaskRepository

class AddTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(title: String): Result<Long> {
        return repository.addTask(title)
    }
}
