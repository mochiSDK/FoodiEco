package com.foodieco.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodieco.ui.composables.EmailTextField
import com.foodieco.ui.composables.LocationTextField
import com.foodieco.ui.composables.Monogram
import com.foodieco.ui.composables.UsernameTextField
import com.foodieco.ui.theme.FoodiEcoTheme

val editIconSize = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var username by remember { mutableStateOf("Username") }    // TODO: put real values.
    var email by remember { mutableStateOf("email@mail.com") }
    var location by remember { mutableStateOf("City, Region") }
    var hasProfilePicture by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Your account") },
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.padding(24.dp)) {
                when (hasProfilePicture) {
                    true -> TODO()
                    false -> Monogram(
                        text = "Username",
                        size = 140.dp,
                        modifier = Modifier
                            .clip(CircleShape)
                    )
                }
                IconButton(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                        .size(editIconSize * 2)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        "Edit icon",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(editIconSize)
                    )
                }
                if (showBottomSheet) {
                    ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                        Column(Modifier.paddingFromBaseline(bottom = 40.dp)) {
                            DropdownMenuItem(
                                onClick = { /*TODO*/ },
                                text = { Text("Take photo") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.CameraAlt,
                                        "Camera icon"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                onClick = { /*TODO*/ },
                                text = { Text("Upload photo") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.FileUpload,
                                        "Upload icon"
                                    )
                                }
                            )
                        }
                    }
                }
            }
            UsernameTextField(
                username = username,
                onValueChange = { username = it },
                modifier = Modifier.padding(8.dp)
            )
            EmailTextField(
                email = email,
                onValueChange = { email = it },
                modifier = Modifier.padding(8.dp)
            )
            LocationTextField(
                location = location,
                onValueChange = { location = it },
                modifier = Modifier.padding(8.dp)
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(Icons.Outlined.Key, "Key icon", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Change password")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    FoodiEcoTheme {
        ProfileScreen()
    }
}
