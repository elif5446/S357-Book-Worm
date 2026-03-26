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
import com.example.book_worm.ui.theme.MajorButton
import com.example.book_worm.ui.theme.MinorButton

@Composable
fun Login(onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val error = " is missing. Please enter a"
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
                            if (email.isEmpty()) {
                                emailError = "Email" + error + "n email address."
                            }
                            if (password.isEmpty()) {
                                passwordError = "Password" + error + " password."
                            }

                            if (emailError.isEmpty() && passwordError.isEmpty()) {}
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
                    emailError + "\n\n" + passwordError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}