package com.example.book_worm

fun isValidEmail(email: String): Boolean {
    return email.matches("""^[a-zA-Z0-9]([a-zA-Z0-9._-]*[a-zA-Z0-9])?@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}${'$'}""".toRegex())
}