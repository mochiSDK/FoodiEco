package com.foodieco.ui.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.foodieco.data.models.SessionStatus
import com.foodieco.ui.composables.NavigationRoute
import com.foodieco.ui.composables.PasswordTextField
import com.foodieco.ui.composables.UsernameTextField
import com.foodieco.ui.theme.capriolaFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
    setUsername: (String) -> Unit,
    setPassword: (String) -> Unit,
    onSignUp: (SessionStatus) -> Unit,
    onCompose: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign up") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Welcome to\n")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append("FoodiEco")
                    }
                },
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontFamily = capriolaFontFamily,
                lineHeight = 40.sp,
                modifier = Modifier.padding(8.dp)
            )
            UsernameTextField(
                username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 8.dp)
            )
            PasswordTextField(
                password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 8.dp)
            )
            Button(
                onClick = {
                    /*
                    * TODO: username and psw will be updated,
                    *  but not the rest of the user.
                    *  Old user should get deleted or sign up should be disabled.
                    */
                    setUsername(username)
                    setPassword(password)
                    onSignUp(SessionStatus.LoggedOut)
                    navController.navigate(NavigationRoute.SignIn.route)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Sign up")
            }
        }
    }
    LaunchedEffect(Unit) {
        onCompose()
    }
}
