package com.xhand.hnu.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.model.entity.BookDetailPost
import com.xhand.hnu.model.entity.BookPost
import com.xhand.hnu.model.entity.Kxjcdata
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.model.entity.Xdjcdata
import com.xhand.hnu.model.entity.Yxjcdata
import com.xhand.hnu.network.GradeService
import com.xhand.hnu.repository.Repository
import com.xhand.hnu.repository.Term
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookUiState(
    val isGettingBook: Boolean = true,
    var booksList: List<Xdjcdata> = emptyList(),
    val isGettingBookDetail: Boolean = true,
    var bookAbleList: List<Kxjcdata> = emptyList(),
    val isGettingBookSelected: Boolean = true,
    var bookSelectedList: List<Yxjcdata> = emptyList(),
    val userInfo: UserInfoEntity? = null,
    val term: Term? = null
)

class BookViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(userInfo = Repository.getToken())
            }
            _uiState.update {
                it.copy(term = Repository.getCurrentTerm())
            }
        }
    }
    var selectTerm by mutableIntStateOf(
        uiState.value.term!!.longGradeTerm.indexOf(
            uiState.value.term!!.nextLongTerm
        )
    )

    // 展示登录框
    var showBookAlert by mutableStateOf(false)
    var showBookSelect by mutableStateOf(false)

    private val gradeService = GradeService.instance()

    suspend fun bookService(xnxqdm: String) {
        try {
            _uiState.update {
                it.copy(isGettingBook = true)
            }
            val res = gradeService.bookDetail(
                body = BookPost("", xnxqdm),
                token = _uiState.value.userInfo?.token ?: ""
            )
            Log.i("TAG667", "$res")
            if (res.code == 200) {
                val booksList = res.xdjcdatas.toMutableList()
                _uiState.update {
                    it.copy(booksList = booksList)
                }
                Log.i("TAG667", "$booksList")
            }
            _uiState.update {
                it.copy(isGettingBook = false)
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    suspend fun bookDetailService(kcrwdm: String, xnxqdm: String) {
        try {
            _uiState.update {
                it.copy(isGettingBookDetail = true)
            }
            val res = gradeService.bookDetail2(
                body = BookDetailPost(kcrwdm, xnxqdm),
                token = _uiState.value.userInfo?.token ?: ""
            )
            Log.i("TAG667", "$res")
            if (res.code == 200) {
                val bookAbleList = res.kxjcdatas.toMutableList()
                _uiState.update {
                    it.copy(bookAbleList = bookAbleList)
                }
                Log.i("TAG667", "$bookAbleList")
            }
            _uiState.update {
                it.copy(isGettingBookDetail = false)
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    suspend fun bookSelectedService(kcrwdm: String, xnxqdm: String) {
        try {
            _uiState.update {
                it.copy(isGettingBookSelected = true)
            }
            val res = gradeService.bookDetail3(
                BookDetailPost(kcrwdm, xnxqdm),
                token = _uiState.value.userInfo?.token ?: ""
            )
            Log.i("TAG667", "$res")
            if (res.code == 200) {
                val bookSelectedList = res.yxjcdatas.toMutableList()
                _uiState.update {
                    it.copy(bookSelectedList = bookSelectedList)
                }
                Log.i("TAG667", "$bookSelectedList")
            }
            _uiState.update {
                it.copy(isGettingBookSelected = false)
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    fun longToShort(xq: String): String {
        return if (xq.length == 6) {
            "${xq.substring(0, 4)}-${xq.substring(0, 4).toInt() + 1}-${xq.last()}"
        } else
            "${xq.substring(0, 4)}0${xq.last()}"
    }

}