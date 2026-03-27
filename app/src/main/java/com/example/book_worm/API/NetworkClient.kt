package com.example.book_worm.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private const val BASE_URL = "https://book-worm-884513973712.northamerica-northeast1.run.app/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authentication: Authentication by lazy {
        retrofit.create(Authentication::class.java)
    }
}

object TokenManager {
    private const val PREFERENCES_NAME = "BookWormPreferences"
    private const val KEY_TOKEN = "authentication_token"

    fun saveToken(context: android.content.Context, token: String) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(context: android.content.Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken(context: android.content.Context) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}