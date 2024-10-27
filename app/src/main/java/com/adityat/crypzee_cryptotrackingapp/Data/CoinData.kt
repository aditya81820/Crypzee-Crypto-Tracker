package com.adityat.crypzee_cryptotrackingapp.Data

data class CoinData(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double,
    val market_cap: Long,
    val market_cap_rank: Int,
    val total_volume: Long,
    val price_change_percentage_24h: Double,
    val total_supply : Double,
    val circulating_supply: Double,
    val high_24h : Double,
    val low_24h: Double
)
