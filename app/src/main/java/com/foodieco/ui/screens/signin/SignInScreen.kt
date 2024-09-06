package com.foodieco.ui.screens.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.foodieco.ui.UserState
import com.foodieco.data.models.SessionStatus
import com.foodieco.ui.composables.NavigationRoute
import com.foodieco.ui.composables.PasswordTextField
import com.foodieco.ui.theme.capriolaFontFamily
import com.foodieco.utils.toSha256

@Composable
fun SignInScreen(
    navController: NavHostController,
    userState: UserState,
    onSignIn: (SessionStatus) -> Unit,
    onCompose: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var isPasswordWrong by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        when {
                            userState.sessionStatus == SessionStatus.LoggedOut -> append("Welcome back,\n")
                            else -> append("Welcome to\n")
                        }
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        when {
                            userState.sessionStatus == SessionStatus.LoggedOut -> append(userState.username)
                            else -> append("FoodiEco")
                        }
                    }
                },
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontFamily = capriolaFontFamily,
                lineHeight = 40.sp,
                modifier = Modifier.padding(8.dp)
            )
            PasswordTextField(
                password,
                onValueChange = { password = it },
                supportingText = "Please try again.",
                isError = isPasswordWrong,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 8.dp)
            )
            Button(
                onClick = {
                    when (userState.password == password.toSha256()) {
                        true -> {
                            isPasswordWrong = false
                            onSignIn(SessionStatus.LoggedIn)
                            navController.popBackStack()
                            navController.navigate(NavigationRoute.Home.route)
                            focusManager.clearFocus()
                        }
                        false -> isPasswordWrong = true
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Sign in")
            }
        }
        TextButton(
            onClick = { navController.navigate(NavigationRoute.SignUp.route) },
            enabled = userState.sessionStatus == SessionStatus.Unknown,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("New user? Sign up")
        }
    }
    LaunchedEffect(Unit) {
        onCompose()
    }
}
