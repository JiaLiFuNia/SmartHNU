package com.smart.htu.screens.application.librarySearch

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.NetworkRepo
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
    val isLoading: Boolean = false,
    val totalPage: Int = 0,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val searchResult: List<LibraryBookListEntity> = emptyList(),
    val searchHistoryList: List<String> = emptyList(),
    val singleBookDetail: List<LibraryBookDetail> = emptyList(),
    val rentList: List<RentBookEntity> = emptyList()
)

@HiltViewModel
class LibrarySearchViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibrarySearchUiState())
    val uiState: StateFlow<LibrarySearchUiState> = _uiState.asStateFlow()

    private val rentBookList = dataStoreRepo.observeRentBookList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeRentBookList().first()
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

    private val searchHistoryList = dataStoreRepo.observeBookSearchHistoryList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeBookSearchHistoryList().first()
            }
        )

    init {
        viewModelScope.launch {
            rentBookList.collect { value ->
                _uiState.update { it.copy(rentList = value) }
            }
        }
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            searchHistoryList.collect { value ->
                _uiState.update { it.copy(searchHistoryList = value) }
            }
        }
    }

    fun addRentBookList(book: RentBookEntity) {
        viewModelScope.launch {
            val currentRentList = _uiState.value.rentList.toMutableList()
            if (currentRentList.contains(book))
                currentRentList.apply { remove(book) }
            else
                if (uiState.value.rentList.size <= 4)
                    currentRentList.apply { add(book) }
            dataStoreRepo.addRentBookList(currentRentList)
            _uiState.update { it.copy(rentList = currentRentList) }
        }
    }


    private var pageNumbers by mutableIntStateOf(1)

    fun librarySearch(keyword: String, page: Int) = viewModelScope.launch {
        Log.i("TAG666", keyword)
        _uiState.update { it.copy(isSearching = true) }
        val res = networkRepo.librarySearch(keyword, page)
        _uiState.update { it.copy(searchResult = res.second) }
        _uiState.update { it.copy(totalPage = if (res.first != "") res.first.toInt() else 0) }
        _uiState.update { it.copy(isSearching = false) }
        Log.i("TAG666", uiState.value.searchResult.size.toString())
    }

    fun loadNextPage(keyword: String) {
        viewModelScope.launch {
            val nextPage = pageNumbers + 1
            pageNumbers = nextPage
            if (nextPage <= _uiState.value.totalPage) {
                val newResults = networkRepo.librarySearch(keyword, nextPage)
                _uiState.update { it.copy(searchResult = uiState.value.searchResult + newResults.second) }
            }
        }
    }

    fun libraryBookDetail(id: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val res = networkRepo.libraryBookDetails(id)
        Log.i("TAG666", res.toString())
        _uiState.update { it.copy(singleBookDetail = res) }
        _uiState.update { it.copy(isLoading = false) }
    }

    fun addSearchHistory(keyword: String) = viewModelScope.launch {
        val currentSearchHistoryList = _uiState.value.searchHistoryList.toMutableList()
        if (currentSearchHistoryList.contains(keyword))
            currentSearchHistoryList.apply { remove(keyword) }
        currentSearchHistoryList.add(0, keyword)
        dataStoreRepo.changeBookSearchHistoryList(currentSearchHistoryList)
        _uiState.update { it.copy(searchHistoryList = currentSearchHistoryList) }
    }

    fun deleteSearchHistory(keywordIndex: Int) = viewModelScope.launch {
        val currentSearchHistoryList = _uiState.value.searchHistoryList.toMutableList()
        currentSearchHistoryList.removeAt(keywordIndex)
        dataStoreRepo.changeBookSearchHistoryList(currentSearchHistoryList)
        _uiState.update { it.copy(searchHistoryList = currentSearchHistoryList) }
    }
}