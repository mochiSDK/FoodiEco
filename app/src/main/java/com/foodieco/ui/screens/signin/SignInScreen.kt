package com.foodieco.ui.screens.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodieco.ui.composables.EmailTextField
import com.foodieco.ui.composables.PasswordTextField
import com.foodieco.ui.theme.FoodiEcoTheme

@Composable
fun SignInScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box {
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
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Text("New user? Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    FoodiEcoTheme {
        SignInScreen()
    }
}
