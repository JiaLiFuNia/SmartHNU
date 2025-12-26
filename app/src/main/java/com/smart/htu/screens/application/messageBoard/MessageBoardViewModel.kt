package com.smart.htu.screens.application.messageBoard

import android.webkit.CookieManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PostDetailData
import com.smart.htu.api.module.PostsListData.PageData
import com.smart.htu.api.module.PostsListData.PostsEntity
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.di.NetworkModule.ApiConstants.MESSAGE_BOARD_BASE_URL
import com.smart.htu.repo.MessageBoardRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageBoardUiState(
    val postsListData: List<PostsEntity>? = null,
    val pageData: PageData? = null,
    val postDetailData: PostDetailData? = null,
    val token: String = ""
)

@HiltViewModel
class MessageBoardViewModel @Inject constructor(
    private val messageBoardRepo: MessageBoardRepo,
    private val networkCookieJar: NetworkCookieJar
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageBoardUiState())

    val uiState: StateFlow<MessageBoardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getMessageBoardPosts()
        }
    }

    suspend fun getMessageBoardPosts(page: Int = 1) {
        messageBoardRepo.getMessageBoardPostsService(page)
            .onSuccess { res ->
                val currentList = _uiState.value.postsListData
                val newList = if (page > 1) {
                    (currentList ?: emptyList()) + res.list
                } else {
                    res.list
                }
                _uiState.update {
                    it.copy(postsListData = newList, pageData = res.page)
                }
            }
    }

    suspend fun getMessageBoardPostDetail(postID: String) {
        // _uiState.update { it.copy(postDetailData = null) }
        messageBoardRepo.getMessageBoardPostDetailService(postID)
            .onSuccess { res ->
                _uiState.update { it.copy(postDetailData = res) }
            }
    }

    suspend fun authLoginToMessageBoard() {
        messageBoardRepo.authLogin()
            .onSuccess { res ->
                _uiState.update { it.copy(token = res) }
                syncCookieToWebView()
            }
    }

    // 同步cookie 到 webview cookieManager
    fun syncCookieToWebView() {
        val cookies = networkCookieJar.loadCookiesForUrl(MESSAGE_BOARD_BASE_URL)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookies.forEach { cookie ->
            cookieManager.setCookie(MESSAGE_BOARD_BASE_URL, "${cookie.name}=${cookie.value}")
        }
        cookieManager.setCookie(MESSAGE_BOARD_BASE_URL, "authorization=${_uiState.value.token}")
        cookieManager.flush()
    }

}