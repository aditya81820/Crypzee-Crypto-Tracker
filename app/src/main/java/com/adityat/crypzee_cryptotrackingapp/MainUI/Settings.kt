package com.adityat.crypzee_cryptotrackingapp.MainUI
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.adityat.crypzee_cryptotrackingapp.Screen
import com.adityat.crypzee_cryptotrackingapp.Viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Settings(
    auth: FirebaseAuth,
    viewModel: MainViewModel,
    navController: NavHostController,
    padd: PaddingValues
) {
    val userName = viewModel.userName.value
    val openAlertDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(padd )
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Hi! ${userName}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingsCard(title = "Account Settings") {
            SettingItem(
                icon = Icons.Default.AccountCircle,
                title = "Manage Account",
                onClick = { /* Open Account Settings */ }
            )
            SettingItem(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy",
                onClick = { /* Open Privacy Settings */ }
            )
        }

        SettingsCard(title = "Appearance") {
            SettingItem(
                icon = Icons.Default.DarkMode,
                title = "Theme",
                onClick = { /* Open Theme Selection */ }
            )
        }

        SettingsCard(title = "Help & Support") {
            SettingItem(
                icon = Icons.Default.Help,
                title = "Contact Us",
                onClick = { /* Open Contact Support */ }
            )
            SettingItem(
                icon = Icons.Default.Help,
                title = "About",
                onClick = {  }
            )
        }
        SettingsCard(title = "Sign Out") {
            SettingItem(
                icon = Icons.Default.Logout,
                title = "Sign Out",
                onClick = {
                    openAlertDialog.value = true
                }
            )
        }

    }
    // Show dialog conditionally
    if (openAlertDialog.value) {
        AlertDialogExample(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                signOutUser(navController) // Add logic here to handle confirmation.
            },
            dialogTitle = "Sign-Out",
            dialogText = "Please Click Confirm button to successfully Sign-Out.",
            icon = Icons.Default.Logout
        )
    }
}

@Composable
fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xff4F75FF),
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
    }
}
private fun signOutUser(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.navigate(Screen.entityScreen.SignUpHomeScreen.route) {
        // Clear back stack
        popUpTo(Screen.entityScreen.SignUpHomeScreen.route) { inclusive = true }
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
                colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.secondary)

            ) {
                Text("Dismiss")
            }
        }
    )
}
