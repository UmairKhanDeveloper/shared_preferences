import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class SharedPreferencesManger(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun putString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: ""
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value)
    }


    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen() {
    val context = LocalContext.current
    val prefs = remember { SharedPreferencesManger(context) }

    var userNameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    var savedUserName by remember { mutableStateOf(prefs.putString("username") ?: "") }
    var savedEmail by remember { mutableStateOf(prefs.putString("email") ?: "") }
    var savedPassword by remember { mutableStateOf(prefs.putString("password") ?: "") }

    var darkMode by remember { mutableStateOf(prefs.getBoolean("dark_mode")) }
    val backgroundColor = if (darkMode) Color(0xFF121212) else Color.White
    val textColor = if (darkMode) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "User Info", color = textColor, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Add navigation action */ }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = textColor)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        prefs.clearAll()
                        userNameInput = ""
                        emailInput = ""
                        passwordInput = ""
                        savedUserName = ""
                        savedEmail = ""
                        savedPassword = ""
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (darkMode) Color.DarkGray else Color.LightGray)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = userNameInput,
                onValueChange = { userNameInput = it },
                label = { Text("Username") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

            )

            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Button(
                onClick = {
                    prefs.saveString("username", userNameInput)
                    prefs.saveString("email", emailInput)
                    prefs.saveString("password", passwordInput)

                    savedUserName = userNameInput
                    savedEmail = emailInput
                    savedPassword = passwordInput

                    Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save Data")
            }

            Divider(thickness = 1.dp, color = Color.Gray)

            if (savedUserName.isNotEmpty()) {
                Text("Username: $savedUserName", color = Color.Gray, fontSize = 18.sp)
                Text("Email: $savedEmail", color = Color.Gray, fontSize = 18.sp)
                Text("Password: $savedPassword", color = Color.Gray, fontSize = 18.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark Mode", color = textColor)
                Spacer(modifier = Modifier.width(8.dp))
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
}
