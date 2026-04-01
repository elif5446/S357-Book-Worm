package com.example.book_worm.DTOs

import java.util.UUID
import java.time.LocalDateTime

data class ReviewCreate(
    val user_id: String,
    val user_name: String,
    val book_id: String,
    val rating: Int,
    val comment: String
)

data class Review(
    val id: String,
    val user_id: String,
    val user_name: String,
    val book_id: String,
    val rating: Int,
    val comment: String,
    val created_at: String
)
