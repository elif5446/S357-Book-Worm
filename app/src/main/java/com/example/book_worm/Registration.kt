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
import com.example.book_worm.ui.theme.Icons
import com.example.book_worm.ui.theme.MajorButton
import com.example.book_worm.ui.theme.MinorButton
import kotlinx.coroutines.launch

@Composable
fun Registration(onNavigateToLogin: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
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
                painter = painterResource(id = R.drawable.registration_background),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    MinorButton("Back to login", onNavigateToLogin, Icons.Back)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp)
                ) {
                    Spacer(modifier = Modifier.height(200.dp))

                    BWTextField(email, { email = it; resetErrors()}, "Email")
                    BWTextField(password, { password = it; resetErrors()  }, "Password")
                    BWTextField(passwordConfirmation, { passwordConfirmation = it; resetErrors() }, "Confirm Password")

                    Spacer(modifier = Modifier.height(100.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MajorButton("Create\nAccount") {
                            if (!isValidEmail(email)) {
                                emailError = "Email is invalid. Please enter a valid email address."
                            }
                            if (password.length < 6) {
                                passwordError = "Password needs to be at least six characters long. Please enter another password."
                            } else if (passwordConfirmation.isEmpty()) {
                                passwordError = "Password confirmation is missing. Please enter your password again."
                            } else if (password != passwordConfirmation) {
                                passwordError = "Passwords are different. Please confirm password by entering it again."
                            }

                            if (emailError.isEmpty() && passwordError.isEmpty()) {
                                scope.launch {
                                    val response = NetworkClient.authentication.register(Credentials(email.lowercase().trim(), password))
                                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                        if (response.isSuccessful) {
                                            val account = response.body()
                                            account?.token?.let {
                                                UserContext.user = account.user
                                                TokenManager.saveToken(context, account.token)
                                                UserContext.token = account.token
                                                // TODO: Navigate to Home Screen
                                            } ?: run {
                                                emailError = "Network error"
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

                    Spacer(modifier = Modifier.height(100.dp))

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
}