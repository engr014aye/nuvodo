package com.anasini.nuvodo.domain.usecase

import com.anasini.nuvodo.domain.repository.TaskRepository

class AutoArchiveCleanSlateUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): Int {
        return repository.autoArchivePreviousDayTasks()
    }
}
