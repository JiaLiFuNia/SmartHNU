package com.smart.htu.screens.application.librarySearch

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.BookBorrowingDetails
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.api.module.SearchBookData
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
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
import javax.inject.Inject

data class LibrarySearchUiState(
    val isSearching: Boolean = false,
    val totalPage: Int = 0,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val searchHistoryList: List<String> = emptyList(),
    val bookSearchList: List<SearchBookData> = emptyList(),
    val libraryBookDetail: LibraryDetailEntity? = null,
    val libraryBookBorrowingDetail: List<BookBorrowingDetails> = emptyList(),
    val waitingBorrowedBookList: List<LibraryDetailEntity> = emptyList()
)

@HiltViewModel
class LibrarySearchViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val libraryNetworkRepo: LibraryNetworkRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibrarySearchUiState())
    val uiState: StateFlow<LibrarySearchUiState> = _uiState.asStateFlow()

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
    }

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
                fetchBookImages(res.dataList ?: emptyList())
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
                    fetchBookImages(res.dataList ?: emptyList())
                }
        }
    }

    private suspend fun fetchBookImages(books: List<SearchBookData>) {
        if (books.isEmpty()) return
        libraryNetworkRepo.libraryBookImgService(
            isbnList = books.map { it.isbn },
            bookIdList = books.map { it.bookId }
        ).onSuccess { imageResults ->
            Log.i("TAG666", "$imageResults")
            val updatedBooks = books.map { book ->
                book.apply {
                    imageUrl = imageResults[book.bookId]?.firstOrNull()?.coverImageUrl ?: ""
                }
            }
            _uiState.update { it.copy(bookSearchList = it.bookSearchList + updatedBooks) }
        }
    }

    fun libraryBookDetail(bookId: String) = viewModelScope.launch {
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
                            imageUrl = imageUrl
                        )
                    )
                }
            }
        libraryNetworkRepo.libraryBookBorrowingDetailService(bookId)
            .onSuccess { res ->
                _uiState.update { it.copy(libraryBookBorrowingDetail = res) }
            }
    }

    fun addSearchHistory(keyword: String) = viewModelScope.launch {
        val currentSearchHistoryList = _uiState.value.searchHistoryList.toMutableList()
        if (currentSearchHistoryList.contains(keyword))
            currentSearchHistoryList.apply { remove(keyword) }
        currentSearchHistoryList.add(0, keyword)
        dataStoreRepo.changeBookSearchHistoryList(currentSearchHistoryList)
    }

    fun deleteSearchHistory(keywordIndex: Int) = viewModelScope.launch {
        val currentSearchHistoryList = _uiState.value.searchHistoryList.toMutableList()
        currentSearchHistoryList.removeAt(keywordIndex)
        dataStoreRepo.changeBookSearchHistoryList(currentSearchHistoryList)
    }

}