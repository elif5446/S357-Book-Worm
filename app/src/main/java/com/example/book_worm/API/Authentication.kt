package com.example.book_worm.API

import com.example.book_worm.DTOs.Account
import com.example.book_worm.DTOs.Credentials
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body

interface Authentication {
    @POST("login/")
    suspend fun login(@Body request: Credentials): Response<Account>

    @POST("register/")
    suspend fun register(@Body request: Credentials): Response<Account>
}