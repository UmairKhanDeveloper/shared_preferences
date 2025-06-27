package com.example.shared_preferences

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val backgroundColor = if (darkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (darkMode) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Preferences", color = textColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize().verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            UserInputField(
                value = nameTextField,
                onValueChange = { nameTextField = it },
                label = "Enter Username",
                textColor = textColor
            )

            UserInputField(
                value = emailTextField,
                onValueChange = { emailTextField = it },
                label = "Enter Email",
                textColor = textColor
            )

            UserInputField(
                value = passwordTextField,
                onValueChange = { passwordTextField = it },
                label = "Enter Password",
                textColor = textColor
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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

            Divider()

            ActionButton("Save Username") {
                prefs.saveString("username", nameTextField)
                userName = nameTextField
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = userName)
            Spacer(modifier = Modifier.height(10.dp))

            ActionButton("Save Email") {
                prefs.saveString("email", emailTextField)
                email = emailTextField
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = email)
            Spacer(modifier = Modifier.height(10.dp))
            ActionButton("Save Password") {
                prefs.saveString("password", passwordTextField)
                password = passwordTextField
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = password)
            Spacer(modifier = Modifier.height(10.dp))

            ActionButton("Share Data") {
                navController.navigate(
                    "dataShowScreen/${Uri.encode(userName)}/${Uri.encode(email)}/${
                        Uri.encode(
                            password
                        )
                    }"
                )
            }

            ActionButton("Clear All") {
                prefs.clear()
                nameTextField = ""
                emailTextField = ""
                passwordTextField = ""
                userName = ""
                email = ""
                password = ""
                darkMode = false
            }
        }
    }
}

@Composable
fun UserInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    textColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = textColor) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = Color.Gray,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}


