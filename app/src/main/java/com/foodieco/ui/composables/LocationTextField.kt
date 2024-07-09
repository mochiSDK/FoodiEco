package com.foodieco.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
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
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = location,
        onValueChange = onValueChange,
        readOnly = true,
        label = { Text("Location") },
        leadingIcon = { Icon(Icons.Outlined.Map, "Map icon") },
        trailingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.LocationOn, "Location icon")
            }
        },
        modifier = modifier
    )
}
