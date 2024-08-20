package com.xhand.hnu.viewmodel


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.model.entity.GradeDetailPost
import com.xhand.hnu.model.entity.GradeInfo
import com.xhand.hnu.model.entity.GradePost
import com.xhand.hnu.model.entity.JDList
import com.xhand.hnu.model.entity.JDPost
import com.xhand.hnu.model.entity.KccjList
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.model.entity.Xscj
import com.xhand.hnu.network.GradeService
import com.xhand.hnu.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TermCheckBoxes(
    val isChecked: Boolean,
    val term: String
)

data class GradeUiState(
    val isShowPersonAlert: Boolean = false,
    val isGettingDetailGrade: Boolean = true,
    var isGettingGrade: Boolean = true,
    val isGettingJD: Boolean = true,
    val gradeDetail: GradeInfo? = null,
    val gradeDetails: Xscj? = null,
    val gradeList: List<KccjList> = emptyList(),
    val jdList: List<JDList> = emptyList(),
    var userInfoEntity: UserInfoEntity? = null,
)

@SuppressLint("MutableCollectionMutableState")
class GradeViewModel : ViewModel() {

    private val term = Repository.getCurrentTerm()
    private val gradeTerm = term.gradeTerm

    val longGradeTerm = term.longGradeTerm
    val currentLongTerm = term.currentLongTerm

    private val _uiState = MutableStateFlow(GradeUiState())
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    private val userInfo = Repository.getToken()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(userInfoEntity = userInfo)
            }
        }
    }

    fun convertTermToIndex(term: MutableList<String>): List<String> {
        term.forEachIndexed { index, _ ->
            val gradeClassString = when (index) {
                0 -> "一"
                1 -> "一"
                2 -> "二"
                3 -> "二"
                4 -> "三"
                5 -> "三"
                6 -> "四"
                7 -> "四"
                else -> ""
            }
            val formOrLater = when ((term.size - index) % 2) {
                0 -> "下"
                else -> "上"
            }
            term[index] = "大${gradeClassString}${formOrLater}"
        }
        return term
    }

    // 成绩详情
    var showPersonAlert by mutableStateOf(false)

    // 成绩列表
    var gradeList by mutableStateOf(mutableListOf<KccjList>())
    var jdList by mutableStateOf(mutableListOf<JDList>())

    // 成绩详情
    var gradeDetail by mutableStateOf(GradeInfo(0.0, "", "", "", "", "", "", "", "", "", 0, ""))
    var gradeDetails by mutableStateOf(Xscj("", 0, 0.0, 0, 0.0))

    private var gradeOrder by mutableStateOf(
        GradeInfo(
            0.0,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            ""
        )
    )

    // 加载中
    var isGettingDetailGrade by mutableStateOf(true)
    var isGettingJD by mutableStateOf(true)

    // 网络请求
    private val gradeService = GradeService.instance()

    // 成绩请求
    suspend fun gradeService() {
        Log.i("TAG666", "gradeService(): gradeTerm: $gradeTerm${_uiState.value.userInfoEntity}")
        gradeList.clear() // 下拉刷新时置空
        var order = 0
        try {
            for (term in gradeTerm) {
                val res = _uiState.value.userInfoEntity?.let {
                    gradeService.gradePost(
                        GradePost(term),
                        token = it.token
                    )
                }
                if (res != null) {
                    Log.i("TAG666", "gradeService(): ${res.kccjList}")
                    if (res.code.toInt() == 200) {
                        for (i in res.kccjList) {
                            order += 1
                            i.order = order
                        }
                        gradeList.addAll(res.kccjList)
                    }
                } else {
                    Log.i("TAG666", "$res")
                }
            }
            _uiState.update { uiState ->
                uiState.copy(gradeList = gradeList)
            }
            _uiState.update { uiState ->
                uiState.copy(isGettingGrade = false)
            }
        } catch (e: Exception) {
            Log.i("TAG666", "gradeService: $e")
        }
    }

    // 成绩请求
    suspend fun gradeDetailService(cjdm: String) {
        try {
            isGettingDetailGrade = true
            val res =
                _uiState.value.userInfoEntity?.let {
                    gradeService.gradeDetail(
                        GradeDetailPost(cjdm),
                        it.token
                    )
                }
            if (res != null) {
                if (res.code == 200) {
                    gradeDetail = res.info1
                    gradeOrder = res.info
                }
            }
            val detail =
                _uiState.value.userInfoEntity?.let {
                    gradeService.gradeDetails(
                        GradeDetailPost(cjdm),
                        it.token
                    )
                }
            if (detail != null) {
                if (detail.code == 200) {
                    gradeDetails = detail.xscj
                }
            }
        } catch (e: Exception) {
            Log.i("TAG666", "gradeDetailService: $e")
        }
        isGettingDetailGrade = false
    }

    suspend fun jDService() {
        try {
            _uiState.update {
                it.copy(userInfoEntity = Repository.getToken())
            }
            Log.i("TAG666", "jDService(): ${_uiState.value.userInfoEntity}")
            val res = _uiState.value.userInfoEntity?.let {
                gradeService.gradeJDDetail(
                    JDPost("1", "01"),
                    it.token
                )
            }
            if (res != null) {
                if (res.code == 200) {
                    jdList = res.list.toMutableList()
                }
                Log.i("TAG666", "jDList: $jdList")
            }
        } catch (e: Exception) {
            Log.i("TAG666", "jDService: $e")
        }
        isGettingJD = false
    }

}