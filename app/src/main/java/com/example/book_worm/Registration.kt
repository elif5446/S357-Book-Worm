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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.book_worm.ui.theme.BWTextField
import com.example.book_worm.ui.theme.Icons
import com.example.book_worm.ui.theme.MajorButton
import com.example.book_worm.ui.theme.MinorButton

@Composable
fun Registration(onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    val error = " is missing. Please enter "
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
                            if (email.isEmpty()) {
                                emailError = "Email" + error + "an email address."
                            }
                            if (password.isEmpty()) {
                                passwordError = "Password" + error + "a password."
                            } else if (passwordConfirmation.isEmpty()) {
                                passwordError = "Password confirmation" + error + "your password again."
                            } else if (password != passwordConfirmation) {
                                passwordError = "Passwords are different. Please confirm password by entering it again."
                            }

                            if (emailError.isEmpty() && passwordError.isEmpty()) {}
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))

                    Text(
                        emailError + "\n\n" + passwordError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}