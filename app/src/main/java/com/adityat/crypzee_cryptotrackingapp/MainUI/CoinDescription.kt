@file:Suppress("NAME_SHADOWING")

package com.adityat.crypzee_cryptotrackingapp.MainUI


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.adityat.crypzee_cryptotrackingapp.Data.CoinData
import com.adityat.crypzee_cryptotrackingapp.R
import com.adityat.crypzee_cryptotrackingapp.Viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

val INTERVALS = mapOf(
    "24H" to 1,
    "1W" to 7,
    "1M" to 30,
    "1Y" to 365,
    "5Y" to 1825
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDescription(
    coin: CoinData?,
    viewModel: MainViewModel,
    navController: NavHostController,
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val context = LocalContext.current
    // Function to add coin to wishlist
    val removebutton = remember {
        mutableStateOf(false)
    }
    fun addToWishlist(coinId: String) {
        val userId = auth.currentUser?.uid ?: return // Get current user ID
        val wishlistRef = db.collection("users").document(userId).collection("wishlist")

        // Add the coin ID to the user's wishlist
        wishlistRef.document(coinId).set(mapOf("coinId" to coinId))
            .addOnSuccessListener {
                // Optionally show a success message or update UI
                Toast.makeText(context, "added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Optionally handle errors
                Toast.makeText(context, "failed to add", Toast.LENGTH_SHORT).show()

            }
    }
    fun removeFromWishlist(coinId: String) {
        val userId = auth.currentUser?.uid ?: return // Get current user ID
        val wishlistRef = db.collection("users").document(userId).collection("wishlist")

        // Remove the coin ID from the user's wishlist
        wishlistRef.document(coinId).delete()
            .addOnSuccessListener {
                // Optionally show a success message or update UI
                Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Optionally handle errors
                Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show()
            }
    }

    fun searchinWishlist(coinId: String) {
        val userId = auth.currentUser?.uid ?: return // Get current user ID
        val wishlistRef = db.collection("users").document(userId).collection("wishlist")
        // Check if the coin ID exists in the user's wishlist
        wishlistRef.document(coinId).get()
            .addOnSuccessListener { document ->
                // Coin is in the wishlist
                removebutton.value = document.exists()
        }
    }
    val data = viewModel.coinPriceData
    val isLoading by viewModel.isLoadinggraph // Add an isLoading state in your viewModel

    BackHandler {
        navController.popBackStack()  // Go back to the previous screen
        viewModel.clearCoinPriceData()
    }
    LaunchedEffect(coin?.id) {
        coin?.id?.let {
            viewModel.fetchCoinMarketChart(coin.id, 1)
        }
        coin?.id?.let {
            searchinWishlist(coin.id)
        }
    }


    coin?.let { coin ->
        val valuesend = coin.price_change_percentage_24h < 0
        var selectedPrice by remember { mutableDoubleStateOf(coin.current_price) }
        val color = if (coin.price_change_percentage_24h < 0) {
            Color.Red
        } else {
            Color(0xff32cd32)
        }
        val icon = if (coin.price_change_percentage_24h < 0) {
            Icons.Default.KeyboardArrowDown
        } else {

            Icons.Default.KeyboardArrowUp
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val painter = rememberAsyncImagePainter(
                    model = coin.image,
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.error)
                )

                Image(
                    painter = painter,
                    contentDescription = "",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )
                if(removebutton.value){
                    Text(
                        text = "- Remove from Wishlist",
                        color = Color(0xff4F75FF),
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.clickable {
                            removeFromWishlist(coin.id)
                            removebutton.value=false
                        }
                    )

                }
                else{
                    Text(
                        text = "+ Add to Wishlist",
                        color = Color(0xff4F75FF),
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.clickable {
                            addToWishlist(coin.id)
                            removebutton.value =true
                        }
                    )

                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = coin.name, fontSize = 28.sp)
                    Text(text = coin.symbol, fontSize = 20.sp)
                }
                Row {
                    Text(text = "${coin.price_change_percentage_24h} %", color = color)
                    Icon(imageVector = icon, contentDescription = "", tint = color)
                }
            }

            // Access other properties similarly
            Text(
                text = "$${String.format("%.2f", selectedPrice)}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 32.sp,
                fontWeight = FontWeight.W500
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                CoinPriceChart(data = data.value, valuesend) { price ->
                    selectedPrice = price  // Update the selected price
                }
            }
            var selectedIndex by remember { mutableIntStateOf(0) }
            val labels = listOf("24H", "1W", "1M", "1Y", "5Y")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                labels.forEachIndexed { index, label ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (index == selectedIndex) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.clickable {
                            viewModel.clearCoinPriceData()
                            selectedIndex = index
                            val days = INTERVALS[label] ?: 1
                            viewModel.fetchCoinMarketChart(
                                coin.id,
                                days
                            ) // Pass the days dynamically
                        }
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(6.dp),
                            color = if (index == selectedIndex) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            Text(
                text = "Value Figures",
                modifier = Modifier.padding(20.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.W600
            )
            Row(

                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth()

            ) {
                Card(
                    modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Rank")
                        Text(text = coin.market_cap_rank.toString(), fontWeight = FontWeight.W700)
                    }

                }
                Card(
                    modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Market Cap")
                        // Convert market cap to billions and format it
                        val marketCapInBillions =
                            coin.market_cap / 1_000_000_000.0 // Ensure division is done in double
                        Text(
                            text = "$${
                                String.format(
                                    "%.2f",
                                    marketCapInBillions
                                )
                            } B", fontWeight = FontWeight.W700
                        ) // Format to two decimal places
                    }
                }

                Card(
                    modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Total Supply")
                        val totalCapInBillions =
                            coin.total_supply / 1_000_000_000.0 // Ensure division is done in double
                        Text(
                            text = "$${String.format("%.2f", totalCapInBillions)} B",
                            fontWeight = FontWeight.W700
                        )
                    }

                }
                Card(
                    modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Circulating Supply")
                        val cirCapInBillions =
                            coin.circulating_supply / 1_000_000_000.0 // Ensure division is done in double
                        Text(
                            text = "$${String.format("%.2f", cirCapInBillions)} B",
                            fontWeight = FontWeight.W700
                        )
                    }

                }
                Card(
                    modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Highest 24H")
                        Text(text = "$${coin.high_24h}", fontWeight = FontWeight.W700)
                    }

                }
                Card(
                    modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Lowest 24H")
                        Text(text = "$${coin.low_24h}", fontWeight = FontWeight.W700)
                    }

                }

            }


        }
    } ?: run {
        Text(text = "Error loading coin details")
    }

}

