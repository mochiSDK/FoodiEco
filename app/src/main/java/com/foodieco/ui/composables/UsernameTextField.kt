package com.foodieco.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun UsernameTextField(
    username: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showClearIcon: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = username,
        onValueChange = onValueChange,
        label = { Text("Username") },
        leadingIcon = { Icon(Icons.Outlined.Person, "Person icon") },
        trailingIcon = {
            AnimatedVisibility(
                visible = showClearIcon && isFocused && username.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Outlined.Cancel, "Cancel text icon")
                }
            }
        },
        modifier = modifier.onFocusChanged { focusState -> isFocused = focusState.isFocused }
    )
}
