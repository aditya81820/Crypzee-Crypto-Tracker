package com.adityat.crypzee_cryptotrackingapp.SignUporsigninScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.adityat.crypzee_cryptotrackingapp.R
import com.adityat.crypzee_cryptotrackingapp.Screen

@Composable
fun SignUphomepage(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Track Your Crypto Portfolio",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 32.sp,
                fontWeight = FontWeight.W600
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.signinimage),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
                )

                Button(
                    onClick = { navController.navigate(Screen.entityScreen.SignUpScreen.eroute) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContentColor = Color.White
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(10.dp)


                ) {
                    Text(text = "Sign Up")

                }
            }
            Row {
                Text(text = "Already Have an Account? ", color = Color.DarkGray)
                Text(
                    text = "Sign In",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable { navController.navigate(Screen.entityScreen.SignInScreen.eroute)})
            }

        }
    }

}
//@Preview
//@Composable
//fun SignUphomePreview(){
//    SignUphomepage(navController)
//}
