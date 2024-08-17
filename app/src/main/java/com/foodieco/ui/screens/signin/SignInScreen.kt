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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.foodieco.ui.composables.NavigationRoute
import com.foodieco.ui.composables.PasswordTextField

@Composable
fun SignInScreen(navController: NavHostController) {
    var password by remember { mutableStateOf("") }
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Welcome back, USERNAME")   // TODO: put real username
            PasswordTextField(password, onValueChange = { password = it})
            Button(onClick = { navController.navigate(NavigationRoute.Home.route) }) {
                Text("Sign in")
            }
        }
        TextButton(
            onClick = { navController.navigate(NavigationRoute.SignUp.route) },
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Text("New user? Sign up")
        }
    }
}
