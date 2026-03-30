package com.example.book_worm.DTOs

import java.util.UUID

data class BookClub(
    val id: java.util.UUID,
    val name: String,
    val memberCount: Int,
    val bookCount: Int,
    val activeSince: String,
    val currentlyReading: String = "",
    val imageUrl: String? = null,
    val drawableRes: Int? = null,
    val nameImageRes: Int? = null
)



