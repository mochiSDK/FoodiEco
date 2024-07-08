package com.foodieco.ui.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.foodieco.ui.composables.EmailTextField
import com.foodieco.ui.composables.PasswordTextField
import com.foodieco.ui.composables.UsernameTextField
import com.foodieco.ui.theme.FoodiEcoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen() {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign up") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back arrow button")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(text = "Welcome to FoodiEco")
            UsernameTextField(username, onValueChange = { username = it })
            EmailTextField(email, onValueChange = { email = it })
            PasswordTextField(password, onValueChange = { password = it})
            Button(onClick = { /*TODO*/ }) {
                Text("Sign Up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    FoodiEcoTheme {
        SignUpScreen()
    }
}
