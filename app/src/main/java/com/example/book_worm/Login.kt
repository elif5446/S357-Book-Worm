package com.example.book_worm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.book_worm.API.NetworkClient
import com.example.book_worm.API.TokenManager
import com.example.book_worm.DTOs.Credentials
import com.example.book_worm.ui.theme.BWTextField
import com.example.book_worm.ui.theme.MajorButton
import com.example.book_worm.ui.theme.MinorButton
import kotlinx.coroutines.launch

@Composable
fun Login(onNavigateToRegister: () -> Unit, onLoginSuccess: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    fun resetErrors() {
        emailError = ""
        passwordError = ""
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.login_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp)
                ) {
                    Spacer(modifier = Modifier.height(275.dp))

                    BWTextField(email, { email = it; resetErrors() }, "Email")
                    BWTextField(password, { password = it; resetErrors() }, "Password")

                    Spacer(modifier = Modifier.height(25.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MajorButton("Login") {
                            val trimmedEmail = email.lowercase().trim()
                            if (!isValidEmail(trimmedEmail)) {
                                emailError = "Email is invalid. Please enter a valid email address."
                            }
                            if (password.length < 6) {
                                passwordError = "Password needs to be at least six characters long. Please enter another password."
                            }

                            if (emailError.isEmpty() && passwordError.isEmpty()) {
                                scope.launch {
                                    val response = NetworkClient.authentication.login(Credentials(trimmedEmail, password))
                                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                        if (response.isSuccessful) {
                                            val account = response.body()
                                            account?.token?.let {
                                                account.user?.let { user ->
                                                    UserContext.user = user
                                                    TokenManager.saveUser(context, user)
                                                }
                                                TokenManager.saveToken(context, account.token)
                                                UserContext.token = account.token
                                                onLoginSuccess()
                                            } ?: run {
                                                emailError = "The password for this email is incorrect. Please enter the correct password or tap \"Forgot Password?\". You can also register with another email address."
                                            }
                                        } else {
                                            val error = response.errorBody()?.string()
                                            try {
                                                error?.let {
                                                    emailError = org.json.JSONObject(error).getString("detail")
                                                } ?: run {
                                                    emailError = "Network Error"
                                                }
                                            } catch (e: Exception) {
                                                emailError = "Server Error: ${response.code()}"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(33.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MinorButton("Forgot Password?", onClick = {})
                }

                Spacer(modifier = Modifier.height(10.dp))

                MajorButton("Create an Account", onNavigateToRegister)

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    if (emailError.isEmpty()) passwordError else (emailError + "\n\n" + passwordError),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
