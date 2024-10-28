package com.adityat.crypzee_cryptotrackingapp.Viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityat.crypzee_cryptotrackingapp.Article
import com.adityat.crypzee_cryptotrackingapp.Data.CoinData
import com.adityat.crypzee_cryptotrackingapp.Screen
import com.adityat.crypzee_cryptotrackingapp.coinsService
import com.adityat.crypzee_cryptotrackingapp.newsService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MainViewModel : ViewModel() {

    // For Bottom Navigation
    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreens.Home)
    val currentScreen: MutableState<Screen> get() = _currentScreen

    // API and Data Handling for coin
    val coinList: MutableState<List<CoinData>> = mutableStateOf(emptyList())
    val isLoading = mutableStateOf(true)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        fetchCoins()
        fetchGainerCoins()
        fetchLosersCoins()
    }


    private fun fetchCoins() {
        viewModelScope.launch {
            isLoading.value = true
            delay(2000)
            try {
                val coins = coinsService.getMarketData()
                coinList.value = coins
                errorMessage.value = null // Clear error if successful
            } catch (e: Exception) {
                errorMessage.value = "Error loading data: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    //Data handling for get top Gainers
    val gainercoinlist: MutableState<List<CoinData>> = mutableStateOf(emptyList())
    val isLoadinggainer = mutableStateOf(true)
    val errorMessagegainer = mutableStateOf<String?>(null)

    private fun fetchGainerCoins() {
        viewModelScope.launch {
            delay(2000)
            try {
                val gainercoin = coinsService.getTopGainers()
                    .filter { (it.price_change_percentage_24h) > 0 }  // Only positive percentage gainers
                    .sortedByDescending { it.price_change_percentage_24h }
                gainercoinlist.value = gainercoin
                errorMessagegainer.value = null // Clear error if successful
            } catch (e: Exception) {
                errorMessagegainer.value = "Error loading data: ${e.message}"
            } finally {
                isLoadinggainer.value = false
            }
        }
    }

    //Data handling for get top losers
    val loserscoinlist: MutableState<List<CoinData>> = mutableStateOf(emptyList())
    val isLoadingloser = mutableStateOf(true)
    val errorMessageloser = mutableStateOf<String?>(null)

    private fun fetchLosersCoins() {
        viewModelScope.launch {
            delay(2000)
            try {
                val losercoin = coinsService.getTopGainers()
                    .filter { (it.price_change_percentage_24h) < 0 }  // Only positive percentage gainers
                    .sortedBy { it.price_change_percentage_24h }
                loserscoinlist.value = losercoin
                errorMessageloser.value = null // Clear error if successful
            } catch (e: Exception) {
                errorMessageloser.value = "Error loading data: ${e.message}"
            } finally {
                isLoadingloser.value = false
            }
        }
    }


    //Data handling of Coin chart
    private val _coinPriceData: MutableState<List<Pair<Long, Double>>> = mutableStateOf(emptyList())
    val coinPriceData: State<List<Pair<Long, Double>>> get() = _coinPriceData

    // Function to clear data
    fun clearCoinPriceData() {
        _coinPriceData.value = emptyList() // Resetting the data
    }

    // Function to add new data
    fun addCoinPriceData(newData: List<Pair<Long, Double>>) {
        _coinPriceData.value = _coinPriceData.value + newData // Append new data
    }

    private val _isLoadinggraph = mutableStateOf(true)
    val isLoadinggraph: State<Boolean> get() = _isLoadinggraph

    fun fetchCoinMarketChart(coinId: String, days: Int) {
        clearCoinPriceData()
        viewModelScope.launch {
            _isLoadinggraph.value = true
            try {
                val chartData = coinsService.getCoinMarketChart(coinId, days = days)
                addCoinPriceData(chartData.prices.map { Pair(it[0].toLong(), it[1]) })
            } catch (e: HttpException) {
                // Handle error
            } finally {
                _isLoadinggraph.value = false
            }
        }
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    fun fetchUserName(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    _userName.value = document.getString("name")
                }
            }
            .addOnFailureListener { e ->
                _userName.value = "Error: ${e.message}"
            }
    }


    //fetch the data from the firestore
    private val auth = FirebaseAuth.getInstance()

    private val _wishlistCoins = MutableStateFlow<List<CoinData>>(emptyList())
    val wishlistCoins: StateFlow<List<CoinData>> = _wishlistCoins
    private val _isLoadingwishcoins = mutableStateOf(true)
    val isLoadingwishcoins: State<Boolean> get() = _isLoadingwishcoins

    // Fetch wishlist coin IDs from Firebase and retrieve details from CoinGecko API
    fun clearWishList() {
        _wishlistCoins.value = emptyList() // Resetting the data
    }


    fun fetchWishlistCoins() {


        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("wishlist")
            .get()
            .addOnSuccessListener { documents ->
                val coinIds = documents.mapNotNull { it.getString("coinId") }

                // Log the fetched coin IDs for verification
                Log.d("WishlistCoins", "Fetched coin IDs from Firestore: $coinIds")

                if (coinIds.isNotEmpty()) {
                    fetchCoinDetailsFromApi(coinIds)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("WishlistCoins", "Error fetching wishlist coins: ${exception.message}")
            }
    }


    // Use CoinGecko API to get details of each coin in the wishlist
    private fun fetchCoinDetailsFromApi(coinIds: List<String>) {
        viewModelScope.launch {
            _isLoadingwishcoins.value = true
            try {
                val coinIdsString = coinIds.joinToString(",")
                val coinDetails = coinsService.getSavedCoinDetails(coinIds = coinIdsString)
                _wishlistCoins.value = coinDetails
            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
            } finally {
                _isLoadingwishcoins.value = false
            }
        }
    }

    private val _newsArticles = mutableStateOf<List<Article>>(emptyList())
    val newsArticles: State<List<Article>> = _newsArticles
    private val _isLoadingnews = mutableStateOf(true)
    val isLoadingnews: State<Boolean> get() = _isLoadingnews

    fun fetchNews(apiKey: String) {
        viewModelScope.launch {
            _isLoadingnews.value = true
            try {
                // Make the API call to fetch news
                val response = newsService.getCryptoNews(apiKey = apiKey)

                // Handle successful response
                _newsArticles.value = response.articles

            } catch (e: Exception) {
                // Handle general errors (e.g., network issues)
                e.printStackTrace()
            } finally {
                _isLoadingnews.value = false
            }
        }
    }


}
