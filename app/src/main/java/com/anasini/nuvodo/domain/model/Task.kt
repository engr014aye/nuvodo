package com.anasini.nuvodo.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val createdAt: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isArchived: Boolean = false,
    val archivedAt: Long? = null
) {
    val isExpired: Boolean
        get() = isArchived && !isCompleted
}
