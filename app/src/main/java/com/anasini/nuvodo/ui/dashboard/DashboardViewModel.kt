package com.anasini.nuvodo.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anasini.nuvodo.domain.model.Task
import com.anasini.nuvodo.domain.usecase.AddTaskUseCase
import com.anasini.nuvodo.domain.usecase.AutoArchiveCleanSlateUseCase
import com.anasini.nuvodo.domain.usecase.CompleteTaskUseCase
import com.anasini.nuvodo.domain.usecase.DeleteTaskUseCase
import com.anasini.nuvodo.domain.usecase.GetActiveTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getActiveTasksUseCase: GetActiveTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val autoArchiveCleanSlateUseCase: AutoArchiveCleanSlateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Enforce the Clean Slate auto-archive on ViewModel init
        runCleanSlateAutoArchive()
        observeActiveTasks()
    }

    private fun runCleanSlateAutoArchive() {
        viewModelScope.launch {
            try {
                val purgedCount = autoArchiveCleanSlateUseCase()
                if (purgedCount > 0) {
                    _uiState.update { it.copy(autoArchivedCount = purgedCount) }
                }
            } catch (_: Exception) {
                // Silently recover if database is empty/fresh
            }
        }
    }

    private fun observeActiveTasks() {
        viewModelScope.launch {
            getActiveTasksUseCase().collect { taskList ->
                _uiState.update { it.copy(tasks = taskList, isLoading = false) }
            }
        }
    }

    fun onAddTask(title: String) {
        viewModelScope.launch {
            val result = addTaskUseCase(title)
            if (result.isFailure) {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun onCompleteTask(task: Task) {
        viewModelScope.launch {
            completeTaskUseCase(task.id)
        }
    }

    fun onDeleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task.id)
        }
    }

    fun dismissCleanSlateBanner() {
        _uiState.update { it.copy(isCleanSlateBannerDismissed = true) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
