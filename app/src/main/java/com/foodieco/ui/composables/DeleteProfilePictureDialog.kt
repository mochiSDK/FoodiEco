package com.foodieco.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteProfilePictureDialog(show: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (!show) {
        return
    }
    AlertDialog(
        title = { Text("Remove") },
        text = { Text("Do you want to remove your profile picture?") },
        icon = { Icon(Icons.Outlined.Delete, "Delete icon") },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}
