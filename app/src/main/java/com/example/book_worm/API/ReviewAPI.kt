package com.example.book_worm.API

import com.example.book_worm.DTOs.Review
import com.example.book_worm.DTOs.ReviewCreate
import retrofit2.Response
import retrofit2.http.*

interface ReviewAPI {
    @POST("reviews")
    suspend fun createReview(@Body body: ReviewCreate): Response<Review>

    @GET("reviews/book/{book_id}")
    suspend fun getReviewsForBook(@Path("book_id") bookId: String): Response<List<Review>>

    @GET("reviews/{review_id}")
    suspend fun getReview(@Path("review_id") reviewId: String): Response<Review>
}
