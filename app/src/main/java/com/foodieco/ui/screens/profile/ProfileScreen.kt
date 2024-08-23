package com.foodieco.ui.screens.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.camera.utils.rememberCameraLauncher
import com.foodieco.UserState
import com.foodieco.ui.composables.LocationTextField
import com.foodieco.ui.composables.Monogram
import com.foodieco.ui.composables.PasswordTextField
import com.foodieco.ui.composables.UsernameTextField
import com.foodieco.utils.LocationService
import com.foodieco.utils.rememberPermission
import kotlinx.coroutines.launch

val editIconSize = 20.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    var newPassword by remember { mutableStateOf("") }
    var newRepeatedPassword by remember { mutableStateOf("") }
    var arePasswordsNotEqual by remember { mutableStateOf(false) }
    var profilePicture: Uri by remember { mutableStateOf(userState.profilePicture.toUri()) }
    var enableSaveButton by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteProfilePictureDialog by remember { mutableStateOf(false) }
    var openPasswordChangeDialog by remember { mutableStateOf(false) }
    var showUsernameError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val ctx = LocalContext.current
    val mediaPicker = rememberLauncherForActivityResult(
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

    val scope = rememberCoroutineScope()
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
            Box(modifier = Modifier.padding(24.dp)) {
                when (profilePicture != Uri.EMPTY) {
                    true -> {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(ctx)
                                .data(profilePicture)
                                .crossfade(true)
                                .crossfade(1000)
                                .diskCachePolicy(CachePolicy.DISABLED)
                                .memoryCachePolicy(CachePolicy.DISABLED)
                                .build(),
                            contentDescription = "Profile picture",
                            contentScale = ContentScale.Crop,
                            loading = { CircularProgressIndicator() },
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .combinedClickable(
                                    onClick = {
                                        scope.launch { snackBarHostState.showSnackbar("Long press to remove profile picture") }
                                    },
                                    onLongClick = { showDeleteProfilePictureDialog = true }
                                )
                        )
                    }
                    false -> Monogram(
                        text = userState.username[0].toString(),
                        size = 140.dp,
                        modifier = Modifier.clip(CircleShape)
                    )
                }
                if (showDeleteProfilePictureDialog) {
                    AlertDialog(
                        title = { Text("Remove") },
                        text = { Text("Do you want to remove your profile picture?")},
                        onDismissRequest = { showDeleteProfilePictureDialog = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    profilePicture = Uri.EMPTY
                                    enableSaveButton = true
                                    showDeleteProfilePictureDialog = false
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { showDeleteProfilePictureDialog = false }) {
                                Text("No")
                            }
                        }
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
                val sheetState = rememberModalBottomSheetState()
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false },
                        sheetState = sheetState
                    ) {
                        val cameraLauncher = rememberCameraLauncher()

                        val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
                            if (status.isGranted) {
                                cameraLauncher.captureImage()
                            } else {
                                Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
                            }
                        }

                        fun takePicture() =
                            if (cameraPermission.status.isGranted) {
                                cameraLauncher.captureImage()
                            } else {
                                cameraPermission.launchPermissionRequest()
                            }

                        if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
                            profilePicture = cameraLauncher.capturedImageUri
                            enableSaveButton = true
                        }

                        Column(Modifier.paddingFromBaseline(bottom = 40.dp)) {
                            DropdownMenuItem(
                                onClick = {
                                    takePicture()
                                    // TODO: dismiss the bottom sheet
                                },
                                text = { Text("Take photo") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.CameraAlt,
                                        "Camera icon"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    mediaPicker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                    // Dismisses the bottom sheet
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                },
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
            if (openPasswordChangeDialog) {
                val clearPasswordChangeDialog = {
                    openPasswordChangeDialog = false
                    newPassword = ""
                    newRepeatedPassword = ""
                    arePasswordsNotEqual = false
                }
                AlertDialog(
                    onDismissRequest = clearPasswordChangeDialog,
                    title = { Text("Change password") },
                    icon = { Icon(Icons.Outlined.Key, "Key icon") },
                    text = {
                        Column {
                            PasswordTextField(
                                label = "New password",
                                password = newPassword,
                                onValueChange = { newPassword = it },
                                showLeadingIcon = false,
                                isError = arePasswordsNotEqual
                            )
                            PasswordTextField(
                                label = "Repeat new password",
                                password = newRepeatedPassword,
                                onValueChange = { newRepeatedPassword = it },
                                showLeadingIcon = false,
                                supportingText = "The passwords do not match.",
                                isError = arePasswordsNotEqual
                            )
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = clearPasswordChangeDialog) {
                            Icon(Icons.Outlined.Close, "Close icon")
                            Text("Cancel")
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            arePasswordsNotEqual = newPassword != newRepeatedPassword
                            if (arePasswordsNotEqual) {
                                return@Button
                            }
                            // TODO: Save the changes.
                            clearPasswordChangeDialog()
                        }) {
                            Icon(Icons.Outlined.Check, "Check icon")
                            Text("Save")
                        }
                    }
                )
            }
        }
    }
}
