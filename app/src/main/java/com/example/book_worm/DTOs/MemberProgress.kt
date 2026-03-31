package com.example.book_worm.DTOs

data class MemberProgress(
    val userId: String,
    val username: String?,
    val currentPages: Int,
    val goalPages: Int
)

