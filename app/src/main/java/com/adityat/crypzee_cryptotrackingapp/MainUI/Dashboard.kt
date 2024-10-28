package com.adityat.crypzee_cryptotrackingapp.MainUI

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adityat.crypzee_cryptotrackingapp.Screen
import com.adityat.crypzee_cryptotrackingapp.Viewmodel.MainViewModel
import com.adityat.crypzee_cryptotrackingapp.screenInBottom
import com.adityat.crypzee_cryptotrackingapp.screenIntop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Dashboard(
    navControllerOld: NavHostController,
    viewModel: MainViewModel,
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val navController: NavController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: Screen.BottomScreens.Home.bRoute // Default to Home

    val currentScreen = remember {
        viewModel.currentScreen.value
    }
    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    val bottombar: @Composable () -> Unit = {
        BottomNavigation(
            Modifier.wrapContentSize(),
            backgroundColor = MaterialTheme.colorScheme.primary
        ) {
            screenInBottom.forEach { item ->
                val isSelected =
                    (currentRoute == item.bRoute || item == Screen.BottomScreens.Home &&
                            (currentRoute == Screen.BottomScreens.topScreens.MarketCap.troute ||
                                    currentRoute == Screen.BottomScreens.topScreens.Gainers.troute ||
                                    currentRoute == Screen.BottomScreens.topScreens.Losers.troute))
                val tint =
                    if (isSelected) Color(0xff4F75FF) else MaterialTheme.colorScheme.secondary
                BottomNavigationItem(
                    selected = currentRoute == item.bRoute,
                    onClick = {
                        auth.uid?.let { userId ->
                            viewModel.fetchUserName(userId)
                        }

                        navController.navigate(item.bRoute)
                        title.value = item.bTitle
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = "", tint = tint)
                    },
                    interactionSource = MutableInteractionSource(),
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Black
                )
            }
        }
    }

    Scaffold(


        backgroundColor = MaterialTheme.colorScheme.primary,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title.value, fontSize = 26.sp)
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                if (currentRoute == Screen.entityScreen.Dashboard.eroute || currentRoute == Screen.BottomScreens.Home.route
                    || currentRoute == Screen.BottomScreens.topScreens.MarketCap.troute || currentRoute == Screen.BottomScreens.topScreens.Gainers.troute
                    || currentRoute == Screen.BottomScreens.topScreens.Losers.troute
                ) {
                    BottomNavigation(
                        Modifier.wrapContentSize(),
                        backgroundColor = MaterialTheme.colorScheme.primary
                    ) {
                        screenIntop.forEach { item ->
                            val isSelected =
                                (currentRoute == item.troute || currentRoute == Screen.BottomScreens.Home.bRoute &&
                                        item == Screen.BottomScreens.topScreens.MarketCap)
                            val color =
                                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
                            BottomNavigationItem(
                                selected = currentRoute == item.troute,
                                onClick = {
                                    navController.navigate(item.troute)
                                },
                                icon = {
                                    Card(

                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary

                                        )

                                    ) {
                                        Text(
                                            text = item.ttitile,
                                            color = color,
                                            modifier = Modifier.padding(
                                                top = 14.dp,
                                                bottom = 14.dp,
                                                start = 18.dp,
                                                end = 18.dp
                                            ),
                                        )
                                    }


                                },
                                interactionSource = MutableInteractionSource(),
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color.Black
                            )
                        }
                    }
                }
            }
        },
        bottomBar = bottombar
    ) { padd ->
        // Use the passed controller here, not a new one

        NavHost(
            navController = navController as NavHostController,
            startDestination = Screen.BottomScreens.topScreens.MarketCap.troute,
        ) {
            composable(Screen.BottomScreens.Home.route) {
                MarketCap(navControllerOld, padd, viewModel)
            }
            composable(Screen.BottomScreens.Explore.route) {
                Explore(viewModel, padd)
            }
            composable(Screen.BottomScreens.Settings.route) {
                Settings(auth,viewModel , navControllerOld,padd)
            }
            composable(Screen.BottomScreens.topScreens.MarketCap.troute) {
                MarketCap(navControllerOld, padd, viewModel)
            }
            composable(Screen.BottomScreens.topScreens.Gainers.troute) {
                Gainer(navControllerOld, padd, viewModel)
            }
            composable(Screen.BottomScreens.topScreens.Losers.troute) {
                Losers(navControllerOld, padd, viewModel)
            }
            composable(Screen.BottomScreens.Wishlist.route) {
                WishList(viewModel,padd, navControllerOld)
            }

        }
    }
}
