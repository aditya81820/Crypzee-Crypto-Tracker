package com.adityat.crypzee_cryptotrackingapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// Retrofit instance
private val retrofit = Retrofit.Builder()
    .baseUrl("https://newsapi.org/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val newsService = retrofit.create(NewsApiService::class.java)

// API service interface
interface NewsApiService {
    @GET("/v2/everything")
    suspend fun getCryptoNews(
        @Query("q") query: String = "cryptocurrency",
        @Query("pageSize") pageSize: Int = 30,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}


// Data classes for news response
data class NewsResponse(
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String,
    val url: String,
    val publishedAt: String,
    val source: Source,
    val urlToImage : String

)

data class Source(
    val name: String
)


