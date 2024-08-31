package com.foodieco.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodieco.data.models.SessionStatus
import com.foodieco.data.repositories.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UserState(
    val username: String = "",
    val password: String = "",
    val profilePicture: String = "",
    val location: String = "",
    val sessionStatus: SessionStatus = SessionStatus.LoggedOut
)

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val state = combine(
        repository.username,
        repository.password,
        repository.profilePicture,
        repository.location,
        repository.sessionStatus
    ) { username, password, profilePicture, location, sessionStatus ->
        UserState(username, password, profilePicture, location, sessionStatus)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UserState()
    )

    fun setUsername(username: String) = viewModelScope.launch {
        repository.setUsername(username)
    }

    fun setPassword(password: String) = viewModelScope.launch {
        repository.setPassword(password)
    }

    fun setProfilePicture(picture: Uri) = viewModelScope.launch {
        repository.setProfilePicture(picture)
    }

    fun setLocation(location: String) = viewModelScope.launch {
        repository.setLocation(location)
    }

    fun setSessionStatus(status: SessionStatus) = viewModelScope.launch {
        repository.setSessionStatus(status)
    }
}
