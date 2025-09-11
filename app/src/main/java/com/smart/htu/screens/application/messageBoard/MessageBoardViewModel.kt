package com.smart.htu.screens.application.messageBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PostDetailData
import com.smart.htu.api.module.PostsListData
import com.smart.htu.api.module.PostsListData.PageData
import com.smart.htu.repo.MessageBoardRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageBoardUiState(
    val postsListData: PostsListData? = null,
    val pageData: PageData? = null,
    val postDetailData: PostDetailData? = null
)

@HiltViewModel
class MessageBoardViewModel @Inject constructor(
    private val messageBoardRepo: MessageBoardRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageBoardUiState())

    val uiState: StateFlow<MessageBoardUiState> = _uiState.asStateFlow()

    init {
        getMessageBoardPosts()
    }

    fun getMessageBoardPosts(page: Int = 1) {
        viewModelScope.launch {
            messageBoardRepo.getMessageBoardPostsService(page)
                .onSuccess { res ->
                    _uiState.update {
                        it.copy(postsListData = res, pageData = res.page)
                    }
                }
        }
    }

    fun getMessageBoardPostDetail(postID: String) {
        viewModelScope.launch {
            // _uiState.update { it.copy(postDetailData = null) }
            messageBoardRepo.getMessageBoardPostDetailService(postID)
                .onSuccess { res ->
                    _uiState.update { it.copy(postDetailData = res) }
                }
        }
    }

}