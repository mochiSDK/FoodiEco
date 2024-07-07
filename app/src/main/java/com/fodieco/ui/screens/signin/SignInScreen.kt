package com.fodieco.ui.screens.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.Button
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
import com.fodieco.ui.theme.FodiEcoTheme

@Composable
fun SignInScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Welcome to FoodiEco")
        EmailTextField(email, onValueChange = { email = it })
        PasswordTextField(password, onValueChange = { password = it})
        Button(onClick = { /*TODO*/ }) {
            Text("Sign In")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    FodiEcoTheme {
        SignInScreen()
    }
}
