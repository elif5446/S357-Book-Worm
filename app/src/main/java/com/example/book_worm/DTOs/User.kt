package com.example.book_worm.DTOs
import com.google.gson.annotations.SerializedName

import java.util.UUID

data class Credentials ( // The fields the user is presented with upon login or registration
    val email: String, // Identifier
    val password: String, // Secret
    val username: String? = null // Optional and not always included in the request
)

data class User (
    val ID: UUID,
    val email: String,
    val username: String? = null
    // More...
)

data class Account (
    val token: String?,
    val user: User?
)