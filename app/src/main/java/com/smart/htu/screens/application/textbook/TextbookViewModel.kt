package com.smart.htu.screens.application.textbook

import top.yukonga.miuix.kmp.basic.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.SearchBookData
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.api.module.Textbook
import com.smart.htu.api.module.TextbookEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.LibraryNetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.TermUtil.getCurrentTerm
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

data class TextbookUiState(
    val termCode: String,
    val globalTermCode: String,
    val termList: List<SingleTerm> = emptyList(),
    val courseTaskCode: String = "",
    val courseList: TextbookEntity? = null,
    val selectableList: List<Textbook>? = null,
    val selectedList: List<Textbook>? = null,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val loginJWCState: Int = DEFAULT_LOGIN_STATE,
    val bookSearchList: List<SearchBookData> = emptyList(),
    val isSearching: Boolean = false
)

@HiltViewModel
class TextbookViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val libraryNetworkRepo: LibraryNetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepo: SharedDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TextbookUiState(
            termCode = getCurrentTerm(),
            globalTermCode = getCurrentTerm(),
        )
    )
    val uiState: StateFlow<TextbookUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val termCodeStateFlow = dataStoreRepo.observeGlobalTermCode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeGlobalTermCode().first()
            }
        )

    private val loginJWCStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginJWCState = value) }
            }
        }
        viewModelScope.launch {
            termCodeStateFlow.collect { value ->
                _uiState.update { it.copy(termCode = value) }
            }
        }
        viewModelScope.launch {
            sharedDataRepo.termList
                .collect { value ->
                    _uiState.update { it.copy(termList = value) }
                }
            sharedDataRepo.currentTermCode
                .collect { value ->
                    _uiState.update { it.copy(globalTermCode = value, termCode = value) }
                }
        }
        viewModelScope.launch {
            refreshTermCalendar()
            getTextbook(_uiState.value.termCode)
        }
    }

    suspend fun refreshTermCalendar() {
        sharedDataRepo.refreshTermCalendar()
    }

    suspend fun getTextbook(termCode: String) {
        jwcNetworkRepo.getTextbookService(GlobalTerm(termCode))
            .onSuccess { res ->
                _uiState.update { it.copy(courseList = res) }
            }.onFailure {
                _uiState.update { it.copy(selectedList = emptyList()) }
            }
    }

    suspend fun getSelectableTextbookService(
        courseTaskCode: String,
        termCode: String
    ) {
        jwcNetworkRepo.getSelectableTextbookService(
            termCode = termCode,
            courseTaskCode = courseTaskCode
        ).onSuccess { res ->
            _uiState.update { it.copy(selectableList = res.selectableList) }
        }.onFailure {
            _uiState.update { it.copy(selectedList = emptyList()) }
        }
    }

    suspend fun getSelectedTextbookService(
        courseTaskCode: String,
        termCode: String
    ) {
        jwcNetworkRepo.getSelectedTextbookService(
            termCode = termCode,
            courseTaskCode = courseTaskCode
        ).onSuccess { res ->
            _uiState.update { it.copy(selectedList = res.selectedList) }
        }.onFailure {
            _uiState.update { it.copy(selectedList = emptyList()) }
        }
    }

    suspend fun librarySearch(isbn: String, page: Int) {
        _uiState.update { it.copy(isSearching = true) }
        libraryNetworkRepo.librarySearchService(isbn, page)
            .onSuccess { res ->
                fetchBookImages(res.dataList ?: emptyList(), true)
            }
        _uiState.update { it.copy(isSearching = false) }
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

    suspend fun changeTermCode(termCode: String) {
        _uiState.update { it.copy(courseList = null) }
        _uiState.update { it.copy(termCode = termCode) }
        getTextbook(_uiState.value.termCode)
    }

}