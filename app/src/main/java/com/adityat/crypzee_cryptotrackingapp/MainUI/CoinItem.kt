package com.adityat.crypzee_cryptotrackingapp.MainUI

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.adityat.crypzee_cryptotrackingapp.Data.CoinData
import com.adityat.crypzee_cryptotrackingapp.R
import com.adityat.crypzee_cryptotrackingapp.Screen
import com.adityat.crypzee_cryptotrackingapp.Viewmodel.MainViewModel
import com.google.gson.Gson

@Composable
fun CoinItem(coin: CoinData, navControllerOld: NavHostController, viewModel: MainViewModel) {

    val color = if (coin.price_change_percentage_24h < 0) {
        Color.Red
    } else {
        Color(0xff32cd32)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()

            .clickable {
                val jsonCoin = Uri.encode(Gson().toJson(coin))
                navControllerOld.navigate("${Screen.entityScreen.CoinDescription.route}/${jsonCoin}")

            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    val painter = rememberAsyncImagePainter(
                        model = coin.image,
                        placeholder = painterResource(R.drawable.placeholder),
                        error = painterResource(R.drawable.error)
                    )

                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = coin.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.LightGray,
                                    contentColor = Color.Black
                                ),
                                border = CardDefaults.outlinedCardBorder(enabled = true),
                                shape = RoundedCornerShape(3.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {

                                Text(
                                    text = String.format("%02d", coin.market_cap_rank),
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(1.dp)
                                )
                            }
                            val coinsymbol = coin.symbol
                            val capitalizedString = coinsymbol.uppercase()

                            Text(
                                text = " ${capitalizedString}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Text(text = "$${coin.current_price}", fontWeight = FontWeight.W500)

                Text(
                    text = String.format("%.2f", coin.price_change_percentage_24h) + "%",
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
            }


        }

    }


}

