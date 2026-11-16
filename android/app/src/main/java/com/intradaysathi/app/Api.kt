package com.intradaysathi.app

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

interface SathiApi {
    @GET("disclaimer") suspend fun disclaimer(): JsonObject
    @GET("market/overview") suspend fun overview(): JsonObject
    @GET("stocks/search") suspend fun search(@Query("q") q: String): JsonObject
    @GET("candles/{symbol}") suspend fun candles(@Path("symbol") s: String, @Query("interval") i: String): JsonObject
    @GET("signals/{symbol}") suspend fun signal(@Path("symbol") s: String, @Query("interval") i: String): JsonObject
    @GET("signals") suspend fun signals(@Query("interval") i: String): JsonObject
    @GET("news") suspend fun news(@Query("symbol") s: String?): JsonObject
    @GET("fundamentals/{symbol}") suspend fun fundamentals(@Path("symbol") s: String): JsonObject
    @GET("announcements/{symbol}") suspend fun announcements(@Path("symbol") s: String): JsonObject
    @POST("paper/orders") suspend fun order(@Body body: JsonObject): JsonObject
    @POST("paper/positions/{id}/close") suspend fun closePosition(@Path("id") id: Int): JsonObject
    @GET("paper/portfolio") suspend fun portfolio(): JsonObject
    @GET("paper/trades") suspend fun trades(): JsonObject
    @POST("paper/reset") suspend fun resetAccount(): JsonObject
    @GET("auto/status") suspend fun autoStatus(): JsonObject
    @POST("auto/config") suspend fun autoConfig(@Body body: JsonObject): JsonObject
    @POST("auto/{action}") suspend fun autoAction(@Path("action") action: String): JsonObject
    @GET("ai/performance") suspend fun performance(): JsonObject
    @GET("ai/models") suspend fun models(): JsonObject
    @POST("ai/train") suspend fun train(@Query("interval") i: String): JsonObject
    @POST("ai/retrain") suspend fun retrain(@Query("interval") i: String): JsonObject
    @POST("ai/rollback") suspend fun rollback(@Query("interval") i: String): JsonObject

    companion object {
        fun create(baseUrl: String): SathiApi {

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SathiApi::class.java)
        }
    }   
}
