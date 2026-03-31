package com.example.book_worm.API

import com.example.book_worm.DTOs.Reaction
import com.example.book_worm.DTOs.ReactionCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReactionAPI {
    @POST("reactions")
    suspend fun createReaction(@Body reaction: ReactionCreate): Response<Reaction>

    @GET("reviews/{reviewId}/reactions")
    suspend fun getReactionsForReview(@Path("reviewId") reviewId: String): Response<List<Reaction>>

    @DELETE("reactions/{reactionId}")
    suspend fun deleteReaction(@Path("reactionId") reactionId: String): Response<Void>
}
