package com.anasini.nuvodo.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anasini.nuvodo.domain.model.Task
import com.anasini.nuvodo.domain.usecase.GetArchivedTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ArchiveViewModel(
    private val getArchivedTasksUseCase: GetArchivedTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

    private var rawTasks: List<Task> = emptyList()

    init {
        observeArchivedTasks()
    }

    private fun observeArchivedTasks() {
        viewModelScope.launch {
            getArchivedTasksUseCase().collect { taskList ->
                rawTasks = taskList
                recomputeState()
            }
        }
    }

    fun setFilter(filter: ArchiveFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        recomputeState()
    }

    private fun recomputeState() {
        val total = rawTasks.size
        val completed = rawTasks.count { it.isCompleted }
        val expired = rawTasks.count { it.isExpired }

        val filtered = when (_uiState.value.selectedFilter) {
            ArchiveFilter.ALL -> rawTasks
            ArchiveFilter.COMPLETED -> rawTasks.filter { it.isCompleted }
            ArchiveFilter.EXPIRED -> rawTasks.filter { it.isExpired }
        }

        val grouped = groupTasksByDate(filtered)

        _uiState.update {
            it.copy(
                groupedTasks = grouped,
                totalCount = total,
                completedCount = completed,
                expiredCount = expired,
                isLoading = false
            )
        }
    }

    private fun groupTasksByDate(tasks: List<Task>): List<DateGroupedTasks> {
        val nowCal = Calendar.getInstance()
        val todayYear = nowCal.get(Calendar.YEAR)
        val todayDayOfYear = nowCal.get(Calendar.DAY_OF_YEAR)

        val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterdayYear = yesterdayCal.get(Calendar.YEAR)
        val yesterdayDayOfYear = yesterdayCal.get(Calendar.DAY_OF_YEAR)

        val fullDateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())

        return tasks.groupBy { task ->
            val timestamp = task.completedAt ?: task.archivedAt ?: task.createdAt
            val taskCal = Calendar.getInstance().apply { timeInMillis = timestamp }
            val taskYear = taskCal.get(Calendar.YEAR)
            val taskDayOfYear = taskCal.get(Calendar.DAY_OF_YEAR)

            when {
                taskYear == todayYear && taskDayOfYear == todayDayOfYear -> "Today"
                taskYear == yesterdayYear && taskDayOfYear == yesterdayDayOfYear -> "Yesterday"
                else -> fullDateFormat.format(Date(timestamp))
            }
        }.map { (header, items) ->
            DateGroupedTasks(dateHeader = header, tasks = items)
        }
    }
}
