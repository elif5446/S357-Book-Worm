package com.example.book_worm.DTOs
import com.google.gson.annotations.SerializedName

import java.util.UUID

data class Credentials (
    val email: String,
    val password: String
)

data class User (
    val ID: UUID,
    val email: String,
    val firstName: String?,
    val lastName: String?
)

data class Account (
    val token: String?,
    val user: User?
)