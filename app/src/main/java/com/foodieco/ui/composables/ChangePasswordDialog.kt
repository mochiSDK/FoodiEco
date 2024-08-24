package com.foodieco.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ChangePasswordDialog(show: Boolean, onDismiss: () -> Unit) {
    if (!show) {
        return
    }
    var newPassword by remember { mutableStateOf("") }
    var newRepeatedPassword by remember { mutableStateOf("") }
    var arePasswordsNotEqual by remember { mutableStateOf(false) }
    val clearPasswordChangeDialog = {
        newPassword = ""
        newRepeatedPassword = ""
        arePasswordsNotEqual = false
        onDismiss()
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
