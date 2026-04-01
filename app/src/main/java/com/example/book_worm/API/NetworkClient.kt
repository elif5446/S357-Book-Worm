package com.example.book_worm.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.content.edit
import com.example.book_worm.DTOs.User
import java.util.UUID

object NetworkClient {
    // Use Cloud URL for production, local URL for development
    private val BASE_URL = "https://book-worm-884513973712.northamerica-northeast1.run.app/"
    // private val LOCAL_BASE_URL = "http://10.0.2.2:8000/" // For Android emulator to connect to localhost:8000
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /* private val localRetrofit = Retrofit.Builder()
        .baseUrl(LOCAL_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build() */

    val authentication: Authentication by lazy {
        retrofit.create(Authentication::class.java)
    }

    val bookClub: BookClubAPI by lazy {
        retrofit.create(BookClubAPI::class.java)
    }

    val review: ReviewAPI by lazy {
        /* localRetrofit */retrofit.create(ReviewAPI::class.java)
    }

    val reaction: ReactionAPI by lazy {
        /* localRetrofit */retrofit.create(ReactionAPI::class.java)
    }

    val comment: CommentAPI by lazy {
        /* localRetrofit */retrofit.create(CommentAPI::class.java)
    }
}

object TokenManager {
    private const val PREFERENCES_NAME = "BookWormPreferences"
    private const val KEY_TOKEN = "authentication_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_USERNAME = "user_username"

    fun saveToken(context: android.content.Context, token: String) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_TOKEN, token) }
    }

    fun saveUser(context: android.content.Context, user: User) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_USER_ID, user.ID.toString())
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_USERNAME, user.username)
        }
    }

    fun getUser(context: android.content.Context): User? {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        val id = prefs.getString(KEY_USER_ID, null) ?: return null
        val email = prefs.getString(KEY_USER_EMAIL, null) ?: return null
        val username = prefs.getString(KEY_USER_USERNAME, null)

        return try {
            User(
                ID = UUID.fromString(id),
                email = email,
                username = username
            )
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    fun getToken(context: android.content.Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken(context: android.content.Context) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit {
            remove(KEY_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_USERNAME)
        }
    }
}
