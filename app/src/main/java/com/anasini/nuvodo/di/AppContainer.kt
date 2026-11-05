package com.anasini.nuvodo.di

import android.content.Context
import com.anasini.nuvodo.data.local.NuvodoDatabase
import com.anasini.nuvodo.data.repository.TaskRepositoryImpl
import com.anasini.nuvodo.domain.repository.TaskRepository
import com.anasini.nuvodo.domain.usecase.AddTaskUseCase
import com.anasini.nuvodo.domain.usecase.AutoArchiveCleanSlateUseCase
import com.anasini.nuvodo.domain.usecase.CompleteTaskUseCase
import com.anasini.nuvodo.domain.usecase.DeleteTaskUseCase
import com.anasini.nuvodo.domain.usecase.GetActiveTasksUseCase
import com.anasini.nuvodo.domain.usecase.GetArchivedTasksUseCase

class AppContainer(context: Context) {

    private val database: NuvodoDatabase by lazy {
        NuvodoDatabase.getInstance(context)
    }

    val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(database.taskDao())
    }

    val getActiveTasksUseCase: GetActiveTasksUseCase by lazy {
        GetActiveTasksUseCase(taskRepository)
    }

    val getArchivedTasksUseCase: GetArchivedTasksUseCase by lazy {
        GetArchivedTasksUseCase(taskRepository)
    }

    val addTaskUseCase: AddTaskUseCase by lazy {
        AddTaskUseCase(taskRepository)
    }

    val completeTaskUseCase: CompleteTaskUseCase by lazy {
        CompleteTaskUseCase(taskRepository)
    }

    val deleteTaskUseCase: DeleteTaskUseCase by lazy {
        DeleteTaskUseCase(taskRepository)
    }

    val autoArchiveCleanSlateUseCase: AutoArchiveCleanSlateUseCase by lazy {
        AutoArchiveCleanSlateUseCase(taskRepository)
    }
}
