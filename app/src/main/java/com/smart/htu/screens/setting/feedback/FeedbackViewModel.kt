package com.smart.htu.screens.setting.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.FeedbackEntity
import com.smart.htu.api.module.FeedbackType
import com.smart.htu.repo.AppNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedbackUiState(
    val feedbackType: FeedbackType = FeedbackType.FEEDBACK,
    val functionalModule: String = "",
    val detailMessage: String = "",
    val submitterEmail: String = "",
    val isSubmitting: Boolean = false
)

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val appNetworkRepo: AppNetworkRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun submitFeedback(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val feedbackEntity = FeedbackEntity(
                type = _uiState.value.feedbackType.type,
                functionModule = _uiState.value.functionalModule,
                message = _uiState.value.detailMessage,
                email = _uiState.value.submitterEmail
            )
            appNetworkRepo.feedbackService(feedbackEntity)
                .onSuccess {
                    onSuccess(it.message)
                }.onFailure {
                    onError(it.message ?: "Unknown error")
                }
            _uiState.update { it.copy(isSubmitting = false) }
        }
    }

    fun changeFeedbackType(type: FeedbackType) {
        _uiState.update { it.copy(feedbackType = type) }
    }

    fun changeFunctionalModule(functionModule: String) {
        _uiState.update { it.copy(functionalModule = functionModule) }
    }

    fun changeDetailMessage(message: String) {
        _uiState.update { it.copy(detailMessage = message) }
    }

    fun changeSubmitterEmail(email: String) {
        _uiState.update { it.copy(submitterEmail = email) }
    }

}