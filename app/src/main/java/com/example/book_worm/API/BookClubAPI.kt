package com.example.book_worm.API

import com.example.book_worm.DTOs.BookClub
import com.example.book_worm.DTOs.ChapterComment
import com.example.book_worm.DTOs.PostCommentRequest
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

    @GET("bookclubs/{clubId}/comments/{chapterNumber}/")
    suspend fun getChapterComments(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String,
        @Path("chapterNumber") chapterNumber: Int
    ): Response<List<ChapterComment>>

    @POST("bookclubs/{clubId}/comments/")
    suspend fun postChapterComment(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String,
        @Body body: PostCommentRequest
    ): Response<ChapterComment>
}



