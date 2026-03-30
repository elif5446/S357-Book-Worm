package com.example.book_worm.DTOs

data class ReadingProgress(
    val currentPages: Int,
    val goalPages: Int,
    val clubId: String
)

data class UpdateProgressRequest(
    val currentPages: Int,
    val clubId: String
)

