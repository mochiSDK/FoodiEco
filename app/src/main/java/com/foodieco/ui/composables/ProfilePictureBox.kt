package com.foodieco.ui.composables

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.foodieco.UserState
import com.foodieco.ui.screens.profile.editIconSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfilePictureBox(
    ctx: Context,
    scope: CoroutineScope,
    userState: UserState,
    profilePicture: Uri,
    snackBarHostState: SnackbarHostState,
    onLongClick: () -> Unit,
    onEdit: () -> Unit,
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
                            onLongClick = onLongClick
                        )
                )
            }
            false -> Monogram(
                text = userState.username[0].toString(),
                size = 140.dp,
                modifier = Modifier.clip(CircleShape)
            )
        }
        IconButton(
            onClick = onEdit,
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
    }
}
