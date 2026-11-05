package com.anasini.nuvodo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anasini.nuvodo.ui.archive.ArchiveViewModel
import com.anasini.nuvodo.ui.dashboard.DashboardViewModel

class ViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(
                    getActiveTasksUseCase = appContainer.getActiveTasksUseCase,
                    addTaskUseCase = appContainer.addTaskUseCase,
                    completeTaskUseCase = appContainer.completeTaskUseCase,
                    deleteTaskUseCase = appContainer.deleteTaskUseCase,
                    autoArchiveCleanSlateUseCase = appContainer.autoArchiveCleanSlateUseCase
                ) as T
            }
            modelClass.isAssignableFrom(ArchiveViewModel::class.java) -> {
                ArchiveViewModel(
                    getArchivedTasksUseCase = appContainer.getArchivedTasksUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
