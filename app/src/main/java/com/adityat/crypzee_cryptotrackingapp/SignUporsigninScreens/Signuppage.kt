package com.adityat.crypzee_cryptotrackingapp.SignUporsigninScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.adityat.crypzee_cryptotrackingapp.R
import com.adityat.crypzee_cryptotrackingapp.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun Signuppage(navController: NavHostController, auth: FirebaseAuth) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                }
                Text(text = "Sign Up", modifier = Modifier.padding(start = 8.dp), fontSize = 22.sp)

            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Welcome to ", fontSize = 25.sp)
                Text(
                    text = "Crypzee",
                    fontSize = 25.sp,
                    color = Color(0xff9DBDFF),
                    fontWeight = FontWeight.SemiBold
                )

            }

            // Name Field
            Text(text = "Name", modifier = Modifier.padding(top = 16.dp, start = 8.dp))
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    focusedBorderColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                    cursorColor = Color.White
                ),
                value = name, onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = {
                    Text(text = "Enter your Name")
                },
                shape = RoundedCornerShape(12.dp)
            )

            Text(text = "E-mail", modifier = Modifier.padding(top = 16.dp, start = 8.dp))
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    focusedBorderColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                    cursorColor = Color.White
                ),
                value = email, onValueChange = { email = it }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), placeholder = {
                    Text(text = "Enter the Email Here!")
                },
                shape = RoundedCornerShape(12.dp)

            )
            Text(text = "Password", modifier = Modifier.padding(top = 16.dp, start = 8.dp))
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    focusedBorderColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                    cursorColor = Color.White
                ),
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = {
                    Text(text = "Enter the Password Here!")
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                    }
                }
            )
            Button(
                onClick = { signUpUser(email, password,name,  auth, navController , context) }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Create an Account")

            }
            Spacer(modifier = Modifier.size(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Already have an account? ")
                Text(text = "Sign In", color = Color(0xff4F75FF), modifier = Modifier.clickable {
                    navController.navigate(
                        Screen.entityScreen.SignInScreen.eroute
                    )
                })
            }
            Spacer(modifier = Modifier.size(25.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "----------------------- or Continue with ------------------------")
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffD3D3D3)
                ),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "By Continuing, you are Agree to our ")
                    Text(text = "Terms Of Service", color = Color(0xff4F75FF))

                }

            }


        }

    }
}



// Function to handle user sign-up
private fun signUpUser(
    email: String,
    password: String,
    name: String,
    auth: FirebaseAuth,
    navController: NavHostController,
    context: Context
) {
    if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: ""
                    saveUserDataToFirestore(userId, name, email, context)
                    // Sign-up successful
                    Toast.makeText(context, "Sign-up successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.entityScreen.Dashboard.route) {
                        // Clear the back stack
                        popUpTo(Screen.entityScreen.SignUpHomeScreen.route) { inclusive = true }
                        launchSingleTop = true
                    } // Navigate to the home screen or any other screen
                } else {
                    // Handle failure
                    val errorMessage = task.exception?.message ?: "Sign-up failed"
                    if (errorMessage.contains("already exists")) {
                        Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    } else {
        // Show error for empty fields
        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
    }
}

// Function to store user data in Firestore
private fun saveUserDataToFirestore(userId: String, name: String, email: String, context: Context) {
    val firestore = FirebaseFirestore.getInstance()
    val user = hashMapOf(
        "userId" to userId,
        "name" to name,
        "email" to email
    )

    firestore.collection("users").document(userId)
        .set(user)
        .addOnSuccessListener {
            Toast.makeText(context, "User data saved successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}



