package com.foodieco.ui.composables

import android.Manifest
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.foodieco.utils.LocationService
import com.foodieco.utils.PermissionStatus
import com.foodieco.utils.StartMonitoringResult
import com.foodieco.utils.rememberPermission
import java.util.Locale

@Composable
fun LocationTextField(
    location: String,
    onValueChange: (String) -> Unit,
    locationService: LocationService,
    modifier: Modifier = Modifier,
    showClearIcon: Boolean = true
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showLocationDisabledAlert by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }
    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted -> {
                val res = locationService.requestCurrentLocation()
                showLocationDisabledAlert = res ==  StartMonitoringResult.GPSDisabled
            }
            PermissionStatus.Denied -> showPermissionDeniedAlert = true
            PermissionStatus.PermanentlyDenied -> showPermissionPermanentlyDeniedSnackbar = true
            PermissionStatus.Unknown -> {}
        }
    }

    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            val res = locationService.requestCurrentLocation()
            showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    val context = LocalContext.current
    OutlinedTextField(
        value = location,
        onValueChange = onValueChange,
        readOnly = true,
        label = { Text("Location") },
        leadingIcon = {
            IconButton(onClick = ::requestLocation) {
                Icon(Icons.Outlined.LocationOn, "Location icon")
                Log.println(Log.DEBUG, "LATITUDE",
                    (locationService.coordinates?.latitude ?: "-").toString()
                )
                Log.println(Log.DEBUG, "LONGITUDE",
                    (locationService.coordinates?.longitude ?: "-").toString()
                )
                locationService.coordinates?.latitude?.let {
                    locationService.coordinates?.longitude?.let { it1 ->
                        Geocoder(context, Locale.getDefault()).getFromLocation(it, it1, 1)
                            .toString()
                    }
                }?.let { Log.println(Log.DEBUG, "LOCATION", it) }
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

    if (showLocationDisabledAlert) {
        AlertDialog(
            title = { Text("Location disabled") },
            text = { Text("Location must be enabled to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationService.openLocationSettings()
                    showLocationDisabledAlert = false
                }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDisabledAlert = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showLocationDisabledAlert = false }
        )
    }

    if (showPermissionDeniedAlert) {
        AlertDialog(
            title = { Text("Location permission denied") },
            text = { Text("Location permission is required to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationPermission.launchPermissionRequest()
                    showPermissionDeniedAlert = false
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedAlert = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showPermissionDeniedAlert = false }
        )
    }

    if (showPermissionPermanentlyDeniedSnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                "Location permission is required.",
                "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
            showPermissionPermanentlyDeniedSnackbar = false
        }
    }
}
