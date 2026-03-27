package com.example.book_worm

import com.example.book_worm.DTOs.User

class UserContext() {
    companion object {
        var user: User? = null
        var token: String? = null
    }
}