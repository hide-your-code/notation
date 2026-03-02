package com.example.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
)
