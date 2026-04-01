package com.example.book_worm.API

import com.example.book_worm.DTOs.Comment
import com.example.book_worm.DTOs.CommentCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentAPI {
    @POST("review-comments")
    suspend fun createComment(@Body comment: CommentCreate): Response<Comment>

    @GET("reviews/{reviewId}/review-comments")
    suspend fun getCommentsForReview(@Path("reviewId") reviewId: String): Response<List<Comment>>

    @DELETE("review-comments/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: String): Response<Void>
}
