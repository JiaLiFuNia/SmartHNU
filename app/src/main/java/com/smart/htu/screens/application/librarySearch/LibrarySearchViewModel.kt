package com.smart.htu.screens.application.librarySearch

import android.util.Log
import android.webkit.CookieManager
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.BookBorrowingDetails
import com.smart.htu.api.module.LibraryBorrowedBookRes.BorrowedBookEntity
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.api.module.SearchBookData
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.di.NetworkModule.ApiConstants.LIBRARY_BASE_URL
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.LibraryNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import javax.inject.Inject

data class LibrarySearchUiState(
    val isSearching: Boolean = false,
    val totalPage: Int = 0,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val searchHistoryList: List<String> = emptyList(),
    val bookSearchList: List<SearchBookData> = emptyList(),
    val libraryBookDetail: LibraryDetailEntity? = null,
    val libraryBookBorrowingDetail: List<BookBorrowingDetails> = emptyList(),
    val waitingBorrowedBookList: List<LibraryDetailEntity> = emptyList(),
    val borrowedBookList: List<BorrowedBookEntity>? = null,
    val currentBorrowingBookList: List<BorrowedBookEntity>? = null,
    val session: String = "",
    val libraryLoginState: Int = DEFAULT_LOGIN_STATE, // 0 未登录 1 登录成功 -1 登录失败 -2 token过期
)

@HiltViewModel
class LibrarySearchViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val libraryNetworkRepo: LibraryNetworkRepo,
    private val networkCookieJar: NetworkCookieJar
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibrarySearchUiState())
    val uiState: StateFlow<LibrarySearchUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()

    private val waitingBorrowedBookListStateFlow = dataStoreRepo.observeWaitingBorrowedBookList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeWaitingBorrowedBookList().first()
            }
        )

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val searchHistoryListStateFlow = dataStoreRepo.observeBookSearchHistoryList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeBookSearchHistoryList().first()
            }
        )

    private val loginLibStateStateFlow = dataStoreRepo.observeLoginLibraryState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginLibraryState().first()
            }
        )

    private val librarySessionStateFLow = dataStoreRepo.observeLibrarySession()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLibrarySession().first()
            }
        )

    init {
        viewModelScope.launch {
            waitingBorrowedBookListStateFlow.collect { value ->
                _uiState.update { it.copy(waitingBorrowedBookList = value) }
            }
        }
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            searchHistoryListStateFlow.collect { value ->
                _uiState.update { it.copy(searchHistoryList = value) }
            }
        }
        viewModelScope.launch {
            loginLibStateStateFlow.collect { value ->
                _uiState.update { it.copy(libraryLoginState = value) }
            }
        }
        viewModelScope.launch {
            librarySessionStateFLow.collect { value ->
                // _uiState.update { it.copy(session = value) }
            }
        }
        viewModelScope.launch {
            if (_uiState.value.libraryLoginState == 1) {
                getLibraryBorrowedBook()
                getCurrentBorrowingBook()
            }
        }
    }

    suspend fun createSession() {
        libraryNetworkRepo.getLoginPage()
            .onSuccess { res ->
                Log.i("TAG666", "createSession: $res")
                // 用于同步验证码
                _uiState.update { it.copy(session = res) }
            }
    }

    suspend fun libraryLogin(
        username: String,
        password: String,
        verifyCode: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        libraryNetworkRepo.libraryLogin(
            username, password, verifyCode
        ).onSuccess { res ->
            changeLoginLibraryState(1)
            onSuccess()
        }.onFailure {
            onFailure(it.message.toString())
            changeLoginLibraryState(-1)
        }
    }

    suspend fun getCurrentBorrowingBook() {
        libraryNetworkRepo.libraryCurrentBorrowingBookService(page = 1, pageSize = 100)
            .onSuccess { res ->
                _uiState.update { it.copy(currentBorrowingBookList = res?.data?.items) }
            }
            .onFailure {
                changeLoginLibraryState(-2)
                Log.d("TAG666", "获取借阅信息失败：${it.message}")
            }
    }

    suspend fun getLibraryBorrowedBook(page: Int = 1, pageSize: Int = 5) {
        libraryNetworkRepo.libraryBorrowedBookService(page, pageSize)
            .onSuccess { res ->
                _uiState.update { it.copy(borrowedBookList = res?.data?.items) }
            }
            .onFailure {
                changeLoginLibraryState(-2)
                Log.d("TAG666", "获取借阅信息失败：${it.message}")
            }
    }

    // 添加待借书籍列表
    fun addWaitingBorrowedBookList(book: LibraryDetailEntity) {
        viewModelScope.launch {
            val currentRentList = _uiState.value.waitingBorrowedBookList.toMutableList()
            if (currentRentList.map { it.bookId }.contains(book.bookId))
                currentRentList.apply { remove(book) }
            else
                currentRentList.apply { add(book) }
            dataStoreRepo.addWaitingBorrowedBookList(currentRentList)
        }
    }

    private var pageNumber = mutableIntStateOf(1)

    fun librarySearch(keyword: String, page: Int) = viewModelScope.launch {
        _uiState.update { it.copy(isSearching = true) }
        libraryNetworkRepo.librarySearchService(keyword, page)
            .onSuccess { res ->
                _uiState.update {
                    it.copy(totalPage = res.actualTotal)
                }
                fetchBookImages(res.dataList ?: emptyList(), true)
            }
        _uiState.update { it.copy(isSearching = false) }
    }

    fun loadNextPage(keyword: String) = viewModelScope.launch {
        val nextPage = pageNumber.intValue + 1
        if (nextPage <= _uiState.value.totalPage) {
            pageNumber.intValue = nextPage
            libraryNetworkRepo.librarySearchService(keyword, nextPage)
                .onSuccess { res ->
                    _uiState.update { it.copy(totalPage = res.actualTotal) }
                    fetchBookImages(res.dataList ?: emptyList(), false)
                }
        }
    }

    private suspend fun fetchBookImages(books: List<SearchBookData>, isFirstLoad: Boolean = true) {
        if (books.isEmpty()) return
        libraryNetworkRepo.libraryBookImgService(
            isbnList = books.map { it.isbn },
            bookIdList = books.map { it.bookId }
        ).onSuccess { imageResults ->
            val updatedBooks = books.map { book ->
                book.apply {
                    imageUrl = imageResults[book.bookId]?.firstOrNull()?.coverImageUrl ?: ""
                }
            }
            _uiState.update { it.copy(bookSearchList = if (isFirstLoad) updatedBooks else it.bookSearchList + updatedBooks) }
        }
    }

    suspend fun libraryBookDetail(bookId: String) {
        libraryNetworkRepo.libraryBookDetailService(bookId)
            .onSuccess { res ->
                var imageUrl = ""
                libraryNetworkRepo.libraryBookImgService(
                    listOf(res.baseInfo.map.isbn),
                    listOf(bookId)
                ).onSuccess { res ->
                    imageUrl = res[bookId]?.firstOrNull()?.coverImageUrl.toString()
                    Log.i("TAG666", "$res")
                }
                _uiState.update {
                    it.copy(
                        libraryBookDetail = LibraryDetailEntity(
                            title = res.baseInfo.map.title,
                            author = res.baseInfo.map.author,
                            bookId = bookId,
                            isbn = res.baseInfo.map.isbn,
                            tags = res.baseInfo.map.tags,
                            abstract = res.detailInfo.map.abstract,
                            imageUrl = imageUrl,
                            topic = Jsoup.parse(res.detailInfo.map.topic ?: "").text().split(" ")
                        )
                    )
                }
            }
        libraryNetworkRepo.libraryBookBorrowingDetailService(bookId)
            .onSuccess { res ->
                _uiState.update { it.copy(libraryBookBorrowingDetail = res) }
            }
    }

    fun changeLoginLibraryState(state: Int) = viewModelScope.launch {
        dataStoreRepo.changeLoginLibraryState(state)
    }

    fun syncCookieToWebView() {
        val cookies = networkCookieJar.loadCookiesForUrl(LIBRARY_BASE_URL)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookies.forEach { cookie ->
            cookieManager.setCookie(LIBRARY_BASE_URL, "${cookie.name}=${cookie.value}")
        }
        cookieManager.flush()
    }


}