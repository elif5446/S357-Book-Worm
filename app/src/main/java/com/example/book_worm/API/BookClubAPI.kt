package com.example.book_worm.API

import com.example.book_worm.DTOs.BookClub
import com.example.book_worm.DTOs.ReadingProgress
import com.example.book_worm.DTOs.UpdateProgressRequest
import retrofit2.Response
import retrofit2.http.*

interface BookClubAPI {
    @GET("bookclubs/mine/")
    suspend fun getMyBookClubs(@Header("Authorization") token: String): Response<List<BookClub>>

    @GET("bookclubs/{clubId}/progress/")
    suspend fun getClubProgress(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String
    ): Response<ReadingProgress>

    @POST("bookclubs/{clubId}/progress/")
    suspend fun updateProgress(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String,
        @Body body: UpdateProgressRequest
    ): Response<ReadingProgress>
}



