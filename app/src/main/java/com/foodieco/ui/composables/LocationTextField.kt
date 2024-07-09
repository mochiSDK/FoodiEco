package com.foodieco.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LocationTextField(
    location: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showClearIcon: Boolean = true
) {
    OutlinedTextField(
        value = location,
        onValueChange = onValueChange,
        readOnly = true,
        label = { Text("Location") },
        leadingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.LocationOn, "Location icon")
            }
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = showClearIcon && location.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Outlined.Cancel, "Cancel text icon")
                }
            }
        },
        modifier = modifier
    )
}
