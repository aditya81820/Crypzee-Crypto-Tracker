package com.adityat.crypzee_cryptotrackingapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val title: String, val route: String) {

    sealed class BottomScreens(val bTitle: String, val bRoute: String, val icon: ImageVector) :
        Screen(bTitle, bRoute) {
        sealed class topScreens(val ttitile: String, val troute: String) : BottomScreens(ttitile, troute, Icons.Default.Home) {
            object MarketCap : topScreens("MARKETCAP", "marketcap")
            object Gainers : topScreens("GAINERS", "gainers")
            object Losers : topScreens("LOSERS", "losers")

        }

        object Home : BottomScreens(
            "Crypto Coins",
            "home",
            Icons.Default.Home,
        )

        object Wishlist : BottomScreens(
            "Wishlist",
            "wishlist",
            Icons.Default.Favorite,
        )

        object Explore : BottomScreens(
            "Explore",
            "explore",
            Icons.Default.Explore,
        )

        object Settings : BottomScreens(
            "Settings",
            "setting",
            Icons.Default.Menu,
        )
    }


    sealed class entityScreen(val etitile: String, val eroute: String) : Screen(etitile, eroute) {
        object SignInScreen : entityScreen("SIGN-IN-SCREEN", "sign-in-screen")
        object SignUpScreen : entityScreen("SIGN-UP-SCREEN", "sign-up-screen")
        object SignUpHomeScreen : entityScreen("SIGN-UP-HOME-SCREEN", "sign-up-home-screen")

        object Dashboard : entityScreen("Dashboard", "dashboard")
        object CoinDescription : entityScreen("Coin-Description", "coin-description")



    }
}

val screenIntop = listOf(
    Screen.BottomScreens.topScreens.MarketCap,
    Screen.BottomScreens.topScreens.Gainers,
    Screen.BottomScreens.topScreens.Losers,
)

val screenInBottom = listOf(
    Screen.BottomScreens.Home,
    Screen.BottomScreens.Wishlist,
    Screen.BottomScreens.Explore,
    Screen.BottomScreens.Settings
)
