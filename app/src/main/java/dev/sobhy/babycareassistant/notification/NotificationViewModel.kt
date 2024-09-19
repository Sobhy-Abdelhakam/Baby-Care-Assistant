package dev.sobhy.babycareassistant.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.notification.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
): ViewModel() {
    private val _notificationState = MutableStateFlow<NotificationState>(NotificationState.Loading)
    val notificationState = _notificationState.asStateFlow()
    init {
        fetchNotifications()
    }

    private fun fetchNotifications(){
        viewModelScope.launch {
            notificationRepository.getNotifications()
                .catch {e ->
                    _notificationState.value = NotificationState.Error(e.message ?: "Unknown error")
                }
                .collect{notifications ->
                    _notificationState.value = NotificationState.Success(notifications)
                }
        }
    }
}