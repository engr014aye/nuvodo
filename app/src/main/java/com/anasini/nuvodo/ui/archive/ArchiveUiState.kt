package com.anasini.nuvodo.ui.archive

import com.anasini.nuvodo.domain.model.Task

enum class ArchiveFilter {
    ALL,
    COMPLETED,
    EXPIRED
}

data class DateGroupedTasks(
    val dateHeader: String,
    val tasks: List<Task>
)

data class ArchiveUiState(
    val groupedTasks: List<DateGroupedTasks> = emptyList(),
    val totalCount: Int = 0,
    val completedCount: Int = 0,
    val expiredCount: Int = 0,
    val selectedFilter: ArchiveFilter = ArchiveFilter.ALL,
    val isLoading: Boolean = true
)
