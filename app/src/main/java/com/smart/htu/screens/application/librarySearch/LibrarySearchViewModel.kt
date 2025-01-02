package com.smart.htu.screens.application.librarySearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.NetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibrarySearchUiState(
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val searchResult: List<LibraryBookListEntity> = emptyList(),
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

    private val _rentBookList = dataStoreRepo.observeRentBookList().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList(),
    )

    init {
        viewModelScope.launch {
            _rentBookList.collect { value ->
                _uiState.update { it.copy(rentList = value) }
            }
        }
    }

    fun changeRentBookState(book: RentBookEntity, add: Boolean = true) {
        viewModelScope.launch {
            val currentRentList = _uiState.value.rentList.toMutableList()
            if (add)
                currentRentList.apply {
                    add(book)
                }
            else
                currentRentList.apply { remove(book) }
            dataStoreRepo.changeRentBookList(currentRentList)
            _uiState.update { it.copy(rentList = currentRentList) }
        }
    }

    fun librarySearch(keyword: String) = viewModelScope.launch {
        _uiState.update { it.copy(isSearching = true) }
        val res = networkRepo.librarySearch(keyword)
        _uiState.update { it.copy(searchResult = res) }
        _uiState.update { it.copy(isSearching = false) }
        Log.i("TAG666", uiState.value.searchResult.size.toString())
    }

    fun libraryBookDetail(id: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val res = networkRepo.libraryBookDetails(id)
        Log.i("TAG666", res.toString())
        _uiState.update { it.copy(singleBookDetail = res) }
        _uiState.update { it.copy(isLoading = false) }
    }

}