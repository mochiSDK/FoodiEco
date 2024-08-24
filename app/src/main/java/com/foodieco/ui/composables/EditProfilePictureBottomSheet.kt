package com.foodieco.ui.composables

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilePictureBottomSheet(
    scope: CoroutineScope,
    mediaPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    show: Boolean,
    onDismiss: () -> Unit,
    takePicture: () -> Unit
) {
    if (!show) {
        return
    }
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
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
                    mediaPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    // Dismisses the bottom sheet
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
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
