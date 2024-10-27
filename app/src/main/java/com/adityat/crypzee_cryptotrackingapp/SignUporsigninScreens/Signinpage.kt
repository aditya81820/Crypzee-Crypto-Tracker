package com.adityat.crypzee_cryptotrackingapp.SignUporsigninScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.adityat.crypzee_cryptotrackingapp.R
import com.adityat.crypzee_cryptotrackingapp.Screen
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

@Composable
fun SignInPage(navController: NavHostController, auth: FirebaseAuth) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Handle back button click */ }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                }
                Text(text = "Log In", modifier = Modifier.padding(start = 8.dp), fontSize = 22.sp)
            }

            // Welcome Text
            Text(text = "Welcome Again!!", fontSize = 25.sp, modifier = Modifier.padding(8.dp))

            // Email Input
            Text(text = "E-mail", modifier = Modifier.padding(top = 16.dp, start = 8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text(text = "Enter the Email Here!") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    focusedBorderColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                isError = emailError
            )
            if (emailError) {
                Text(
                    text = "Invalid email format",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Password Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Password", modifier = Modifier.padding(top = 16.dp, start = 8.dp))
                Text(text = "Reset Password", modifier = Modifier.padding(top = 16.dp, end = 8.dp), color = Color(0xff4F75FF))
            }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text(text = "Enter the Password Here!") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                    }
                },
                isError = passwordError
            )
            if (passwordError) {
                Text(
                    text = "Password must be at least 6 characters",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Log In Button
            Button(
                onClick = {
                    validateCredentials(email, password, auth, navController, context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Log In")
            }

            // Error message
            if (showError) {
                Text(
                    text = "Invalid email or password",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.size(25.dp))

            // Continue with Google
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "----------------------- or Continue with ------------------------")
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                onClick = { /* Handle Google login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffD3D3D3)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = "   Google")
                }
            }

            // Sign Up Link
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Don't Have an account? ")
                    Text(text = "Create an account", color = Color(0xff4F75FF))
                }
            }
        }
    }
}

// Email validation function
fun isValidEmail(email: String): Boolean {
    val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)\$")
    return emailPattern.matcher(email).matches()
}

// Credentials validation
fun validateCredentials(email: String, password: String, auth: FirebaseAuth, navController: NavHostController, context: Context) {
    if (!isValidEmail(email)) {
        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        return
    }
    if (password.length < 6) {
        Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        return
    }

    // Attempt to sign in with Firebase
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Navigate to the Dashboard
                navController.navigate(Screen.entityScreen.Dashboard.route) {
                    popUpTo(Screen.entityScreen.SignUpHomeScreen.route) { inclusive = true }
                }
                Toast.makeText(context, "Signed in successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "User not exists or Wrong Credentials", Toast.LENGTH_SHORT).show()
            }
        }
}
