package com.example.book_worm.DTOs

import java.time.LocalDateTime

data class ReactionCreate(
    val user_id: String,
    val review_id: String,
    val reaction_type: String
)

data class Reaction(
    val id: String,
    val user_id: String,
    val review_id: String,
    val reaction_type: String,
    val created_at: String
)
