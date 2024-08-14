package com.xhand.hnu.viewmodel


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.model.UserInfoManager
import com.xhand.hnu.model.entity.GradeDetailPost
import com.xhand.hnu.model.entity.GradeInfo
import com.xhand.hnu.model.entity.GradePost
import com.xhand.hnu.model.entity.JDList
import com.xhand.hnu.model.entity.JDPost
import com.xhand.hnu.model.entity.KccjList
import com.xhand.hnu.model.entity.Xscj
import com.xhand.hnu.network.GradeService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class TermCheckBoxes(
    val isChecked: Boolean,
    val term: String
)

@SuppressLint("MutableCollectionMutableState")
class GradeViewModel @Inject constructor(
    settingsViewModel: SettingsViewModel,
    context: Context
) : ViewModel() {

    private val userInfoManager = UserInfoManager(context)

    init {
        viewModelScope.launch {
            val userInfoStore = userInfoManager.userInfo.firstOrNull()
            userInfo = userInfoStore
        }
    }

    private val grade = settingsViewModel.longGradeTerm
    var checkboxes = mutableStateListOf(
        TermCheckBoxes(
            isChecked = false,
            term = grade[0]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[1]
        ),
        TermCheckBoxes(
            isChecked = true,
            term = grade[2]
        ),
        TermCheckBoxes(
            isChecked = true,
            term = grade[3]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[4]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[5]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[6]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[7]
        )
    )

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

    private val gradeTerm = settingsViewModel.gradeTerm
    private var userInfo = settingsViewModel.userInfo

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
    var isGettingGrade by mutableStateOf(true)
    var isGettingJD by mutableStateOf(true)

    // 网络请求
    private val gradeService = GradeService.instance()

    // 成绩请求
    suspend fun gradeService() {
        Log.i("TAG666", "gradeService(): $gradeTerm")
        gradeList.clear() // 下拉刷新时置空
        var order = 0
        try {
            for (term in gradeTerm) {
                val res = userInfo?.let {
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
                    } else {
                        Log.i("TAG666", "null")
                    }
                }
            }
            isGettingGrade = false
        } catch (e: Exception) {
            isGettingGrade = false
            Log.i("TAG666", "gradeService: $e")
        }

    }

    // 成绩请求
    suspend fun gradeDetailService(cjdm: String) {
        try {
            val res =
                userInfo?.let { gradeService.gradeDetail(GradeDetailPost(cjdm), it.token) }
            if (res != null) {
                if (res.code == 200) {
                    gradeDetail = res.info1
                    gradeOrder = res.info
                }
            }
            val detail =
                userInfo?.let { gradeService.gradeDetails(GradeDetailPost(cjdm), it.token) }
            if (detail != null) {
                if (detail.code == 200) {
                    gradeDetails = detail.xscj
                }
            }
            isGettingDetailGrade = false
        } catch (e: Exception) {
            isGettingDetailGrade = false
            Log.i("TAG666", "gradeDetailService: $e")
        }
    }

    suspend fun jDService() {
        try {
            val res = userInfo?.let { gradeService.gradeJDDetail(JDPost("1", "01"), it.token) }
            if (res != null) {
                if (res.code == 200) {
                    jdList = res.list.toMutableList()
                }
                Log.i("TAG666", "jDList: $jdList")
            }
            isGettingJD = false
        } catch (e: Exception) {
            Log.i("TAG666", "jDService: $e")
        }
    }

}