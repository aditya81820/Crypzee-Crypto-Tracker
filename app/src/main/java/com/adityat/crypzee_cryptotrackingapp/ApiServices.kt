package com.adityat.crypzee_cryptotrackingapp

import com.adityat.crypzee_cryptotrackingapp.Data.CoinData
import com.adityat.crypzee_cryptotrackingapp.Data.CoinMarketChart
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private val retrofit = Retrofit.Builder().baseUrl("https://api.coingecko.com/api/v3/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val coinsService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("coins/markets")
    suspend fun getMarketData(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): List<CoinData>

    @GET("coins/{id}/market_chart")
    suspend fun getCoinMarketChart(
        @Path("id") coinId: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int  // To get data for the last 24 hours
    ): CoinMarketChart

    @GET("coins/markets")
    suspend fun getTopGainers(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "price_change_percentage_24h_desc",  // Order by 24-hour percentage change
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): List<CoinData>


    @GET("coins/markets")
    suspend fun getSavedCoinDetails(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") coinIds: String  // Accept multiple coin IDs
    ): List<CoinData>





}
