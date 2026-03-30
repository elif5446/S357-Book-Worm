package com.example.book_worm.DTOs

data class ChapterComment(
    val id: String = "",
    val clubId: String,
    val chapterNumber: Int,
    val username: String,
    val content: String,
    val createdAt: String = ""
)

data class PostCommentRequest(
    val clubId: String,
    val chapterNumber: Int,
    val content: String
)

