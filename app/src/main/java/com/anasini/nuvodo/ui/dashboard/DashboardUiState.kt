package com.anasini.nuvodo.ui.dashboard

import com.anasini.nuvodo.domain.model.Task

data class DashboardUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val autoArchivedCount: Int = 0,
    val isCleanSlateBannerDismissed: Boolean = false,
    val errorMessage: String? = null
) {
    val canAddTask: Boolean
        get() = tasks.size < 6
}
