package dev.sobhy.babycareassistant.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.authentication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun signOut() = authRepository.logout()
    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getUserProfile()
                .onStart {
                    Log.d("ProfileViewModel", "Fetching user profile started")
                    _state.update { it.copy(isLoading = true) }
                }
                .catch {e ->
                    Log.e("ProfileViewModel", "Error fetching user profile", e)
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collectLatest { result ->
                    result.onSuccess {userProfile ->
                        Log.d("ProfileViewModel", "User profile fetched successfully: $userProfile")
                        _state.update { it.copy(isLoading = false, user = userProfile, error = null) }
                    }
                    result.onFailure {e ->
                        Log.e("ProfileViewModel", "Error fetching user profile", e)
                        _state.update { it.copy(isLoading = false, error = e.message) }
                    }
                }
        }
    }

}