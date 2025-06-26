package com.example.shared_preferences

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


class PreferenceManger(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}

@Composable
fun SharedPreferences(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { PreferenceManger(context) }

    var nameTextField by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf(prefs.getString("username")) }

    var emailTextField by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(prefs.getString("email")) }

    var passwordTextField by remember { mutableStateOf("") }
    var password by remember { mutableStateOf(prefs.getString("password")) }


    var darkMode by remember { mutableStateOf(prefs.getBoolean("dark_mode")) }

    val backgroundColor = if (darkMode) Color.Gray else Color.White
    val textColor = if (darkMode) Color.White else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nameTextField,
            onValueChange = { nameTextField = it },
            label = { Text("Enter Username", color = textColor) },
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = emailTextField,
            onValueChange = { emailTextField = it },
            label = { Text("Enter Email", color = textColor) },
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = passwordTextField,
            onValueChange = { passwordTextField = it },
            label = { Text("Enter password", color = textColor) },
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            prefs.saveString("username", nameTextField)
            userName = nameTextField
        }) {
            Text(text = "Save userName")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            prefs.saveString("email", emailTextField)
            email = emailTextField
        }) {
            Text(text = "Save email")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            prefs.saveString("password", password)
            password = passwordTextField
        }) {
            Text(text = "Save password")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate(
                "dataShowScreen/${Uri.encode(userName)}/${Uri.encode(email)}/${Uri.encode(password)}"
            )
        }) {
            Text(text = "Share data")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            prefs.clear()
            userName = ""
            nameTextField = ""
            email = ""
            emailTextField = ""
            password = ""
            passwordTextField = ""
            darkMode = false
        }) {
            Text(text = "Delete")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Save userName: $userName", color = textColor)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Save email: $email", color = textColor)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Save password: $password", color = textColor)


        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode", color = textColor)
            Spacer(modifier = Modifier.width(10.dp))
            Switch(
                checked = darkMode,
                onCheckedChange = {
                    darkMode = it
                    prefs.saveBoolean("dark_mode", it)
                }
            )
        }
    }
}


