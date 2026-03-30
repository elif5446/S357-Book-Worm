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
fun Login(onNavigateToRegister: () -> Unit, onLoginSuccess: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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

                    BWTextField(email, { email = it; errorMessage = "" }, "Email")
                    BWTextField(password, { password = it; errorMessage = "" }, "Password")

                    Spacer(modifier = Modifier.height(16.dp))

                    // Error message — right inside the apple, always visible
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                        } else {
                            MajorButton("Login") {
                                val trimmedEmail = email.lowercase().trim()
                                var valid = true

                                if (!isValidEmail(trimmedEmail)) {
                                    errorMessage = "Email is invalid. Please enter a valid email address."
                                    valid = false
                                } else if (password.length < 6) {
                                    errorMessage = "Password must be at least 6 characters."
                                    valid = false
                                }

                                if (valid) {
                                    isLoading = true
                                    scope.launch {
                                        try {
                                            val response = NetworkClient.authentication.login(Credentials(trimmedEmail, password))
                                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                                if (response.isSuccessful) {
                                                    val account = response.body()
                                                    account?.token?.let {
                                                        UserContext.user = account.user
                                                        TokenManager.saveToken(context, account.token)
                                                        UserContext.token = account.token
                                                        onLoginSuccess()
                                                    } ?: run {
                                                        errorMessage = "Incorrect email or password."
                                                    }
                                                } else {
                                                    val error = response.errorBody()?.string()
                                                    errorMessage = try {
                                                        error?.let { org.json.JSONObject(it).getString("detail") }
                                                            ?: "Login failed (${response.code()})"
                                                    } catch (e: Exception) {
                                                        "Server error: ${response.code()}"
                                                    }
                                                }
                                                isLoading = false
                                            }
                                        } catch (e: Exception) {
                                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                                errorMessage = "Network error: ${e.message}"
                                                isLoading = false
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MinorButton("Forgot Password?", onClick = {})
                }

                MajorButton("Create an Account", onNavigateToRegister)
            }
        }
    }
}