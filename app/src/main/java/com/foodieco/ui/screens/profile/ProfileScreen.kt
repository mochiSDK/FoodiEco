package com.foodieco.ui.screens.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.foodieco.UserState
import com.foodieco.ui.composables.ChangePasswordDialog
import com.foodieco.ui.composables.DeleteProfilePictureDialog
import com.foodieco.ui.composables.EditProfilePictureBottomSheet
import com.foodieco.ui.composables.LocationTextField
import com.foodieco.ui.composables.ProfilePictureBox
import com.foodieco.ui.composables.UsernameTextField
import com.foodieco.utils.LocationService
import com.foodieco.utils.rememberCameraLauncher
import com.foodieco.utils.rememberPermission

val editIconSize = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userState: UserState,
    locationService: LocationService,
    setUsername: (String) -> Unit,
    setLocation: (String) -> Unit,
    setProfilePicture: (Uri) -> Unit
) {
    var username by remember { mutableStateOf(userState.username) }
    var location by remember { mutableStateOf(userState.location) }
    var profilePicture: Uri by remember { mutableStateOf(userState.profilePicture.toUri()) }
    var enableSaveButton by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteProfilePictureDialog by remember { mutableStateOf(false) }
    var openPasswordChangeDialog by remember { mutableStateOf(false) }
    var showUsernameError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                // TODO: saving to internal storage might be better.
                ctx.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                profilePicture = it
            } else {
                profilePicture = Uri.EMPTY
            }
            enableSaveButton = true
        }
    )

    val cameraLauncher = rememberCameraLauncher()
    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
        profilePicture = cameraLauncher.capturedImageUri
        enableSaveButton = true
    }
    fun takePicture() = when {
        cameraPermission.status.isGranted -> cameraLauncher.captureImage()
        else -> cameraPermission.launchPermissionRequest()
    }

    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Your account") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back arrow button")
                    }
                },
                actions = {
                    IconButton(
                        enabled = enableSaveButton,
                        onClick = {
                            when (username.isBlank()) {
                                true -> {
                                    showUsernameError = true
                                    return@IconButton
                                }
                                false -> setUsername(username)
                            }
                            setProfilePicture(profilePicture)
                            setLocation(location)
                            enableSaveButton = false
                            focusManager.clearFocus()
                        }
                    ) {
                        Icon(Icons.Outlined.Check, "Check icon")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ProfilePictureBox(
                ctx = ctx,
                scope = scope,
                userState = userState,
                profilePicture = profilePicture,
                snackBarHostState = snackBarHostState,
                onLongClick = { showDeleteProfilePictureDialog = true },
                onEdit = { showBottomSheet = true }
            )
            EditProfilePictureBottomSheet(
                scope = scope,
                mediaPickerLauncher = mediaPickerLauncher,
                show = showBottomSheet,
                onDismiss = { showBottomSheet = false },
                takePicture = { takePicture() }
            )
            DeleteProfilePictureDialog(
                show = showDeleteProfilePictureDialog,
                onDismiss = { showDeleteProfilePictureDialog = false },
                onConfirm = {
                    profilePicture = Uri.EMPTY
                    enableSaveButton = true
                    showDeleteProfilePictureDialog = false
                }
            )
            UsernameTextField(
                username = username,
                supportingText = "This field cannot be empty.",
                isError = showUsernameError,
                onValueChange = {
                    username = it
                    enableSaveButton = true
                    showUsernameError = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 8.dp)
            )
            LocationTextField(
                value = location,
                onValueChange = { location = it },
                locationService = locationService,
                onLeadingIconButtonClick = { enableSaveButton = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 8.dp)
            )
            Button(
                onClick = { openPasswordChangeDialog = true },
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(Icons.Outlined.Key, "Key icon", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Change password")
            }
            ChangePasswordDialog(
                show = openPasswordChangeDialog,
                onDismiss = { openPasswordChangeDialog = false }
            )
        }
    }
}
