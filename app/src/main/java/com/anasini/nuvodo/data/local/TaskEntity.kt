package com.anasini.nuvodo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val createdAt: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isArchived: Boolean = false,
    val archivedAt: Long? = null
)
