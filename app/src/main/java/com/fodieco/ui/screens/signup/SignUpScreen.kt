package com.fodieco.ui.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fodieco.ui.composables.EmailTextField
import com.fodieco.ui.composables.PasswordTextField
import com.fodieco.ui.composables.UsernameTextField
import com.fodieco.ui.theme.FodiEcoTheme

@Composable
fun SignUpScreen() {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
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

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    FodiEcoTheme {
        SignUpScreen()
    }
}
