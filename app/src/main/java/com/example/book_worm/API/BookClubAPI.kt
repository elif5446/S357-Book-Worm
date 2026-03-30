package com.example.book_worm.API

import com.example.book_worm.DTOs.BookClub
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface BookClubAPI {
    @GET("bookclubs/mine/")
    suspend fun getMyBookClubs(@Header("Authorization") token: String): Response<List<BookClub>>
}

