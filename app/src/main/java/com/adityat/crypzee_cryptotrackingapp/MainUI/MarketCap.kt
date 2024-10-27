package com.adityat.crypzee_cryptotrackingapp.MainUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.adityat.crypzee_cryptotrackingapp.Viewmodel.MainViewModel


@Composable
fun MarketCap(

    navControllerOld: NavHostController,
    padding: PaddingValues,
    viewModel: MainViewModel,

    ) {
    val coinList by viewModel.coinList
    val isLoading by viewModel.isLoading

    Column(modifier = Modifier.padding(padding)) {
        // Show loading indicator if data is loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White
                )
            }
        } else {
            // Show coin list when not loading
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("CRYPTO COIN", fontSize = 10.sp)
                Text("LAST PRICE", fontSize = 10.sp)
                Text("24H CHANGE", fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.size(10.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(coinList, key = { it.id }) { coin ->
                    CoinItem(coin , navControllerOld , viewModel)
                    Row(modifier = Modifier.padding(8.dp)) {
                    }
                }
            }
        }
    }
}