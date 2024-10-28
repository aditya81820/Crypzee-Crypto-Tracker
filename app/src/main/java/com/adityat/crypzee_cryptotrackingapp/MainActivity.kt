package com.adityat.crypzee_cryptotrackingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adityat.crypzee_cryptotrackingapp.Data.CoinData
import com.adityat.crypzee_cryptotrackingapp.MainUI.CoinDescription
import com.adityat.crypzee_cryptotrackingapp.MainUI.Dashboard
import com.adityat.crypzee_cryptotrackingapp.SignUporsigninScreens.SignInPage
import com.adityat.crypzee_cryptotrackingapp.SignUporsigninScreens.SignUphomepage
import com.adityat.crypzee_cryptotrackingapp.SignUporsigninScreens.Signuppage
import com.adityat.crypzee_cryptotrackingapp.Viewmodel.MainViewModel
import com.adityat.crypzee_cryptotrackingapp.ui.theme.CrypzeeCryptoTrackingAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val auth = FirebaseAuth.getInstance()
            CrypzeeCryptoTrackingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = viewModel()
                    val controller: NavHostController = rememberNavController()

                    // Set up navigation
                    Navigation(controller, viewModel, auth)
                }
            }
        }
    }


}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, auth: FirebaseAuth) {
    val db = FirebaseFirestore.getInstance()
    NavHost(
        navController = navController as NavHostController,
        startDestination = if (auth.currentUser != null) {
            Screen.entityScreen.Dashboard.route // Navigate to Dashboard if logged in
        } else {
            Screen.entityScreen.SignUpHomeScreen.route // Navigate to SignUpHomeScreen if not logged in
        },
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.entityScreen.SignUpHomeScreen.route) {
            SignUphomepage(navController)
        }
        composable(Screen.entityScreen.SignInScreen.route) {
            SignInPage(navController, auth)
        }
        composable(Screen.entityScreen.Dashboard.route) {
            Dashboard(navController, viewModel,auth,db)
        }
        composable(Screen.entityScreen.SignUpScreen.route) {
            Signuppage(navController, auth)
        }
        composable(
            route = "${Screen.entityScreen.CoinDescription.route}/{coinJson}",
            arguments = listOf(navArgument("coinJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val coinJson = backStackEntry.arguments?.getString("coinJson")
            val coinData = coinJson?.let { Gson().fromJson(it, CoinData::class.java) }
            CoinDescription(coinData, viewModel, navController,auth,db)
        }
    }
}
