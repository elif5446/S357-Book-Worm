package com.example.book_worm.DTOs

data class CommentCreate(
    val user_id: String,
    val user_name: String,
    val review_id: String,
    val comment_text: String
)

data class Comment(
    val id: String,
    val user_id: String,
    val user_name: String,
    val review_id: String,
    val comment_text: String,
    val created_at: String
)
