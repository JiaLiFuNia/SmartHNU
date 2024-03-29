package com.xhand.hnu2.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu2.R
import com.xhand.hnu2.components.RSAEncryptionHelper
import com.xhand.hnu2.model.entity.ArticleListEntity
import com.xhand.hnu2.model.entity.GradeDetailEntity
import com.xhand.hnu2.model.entity.GradeDetailPost
import com.xhand.hnu2.model.entity.GradeInfo
import com.xhand.hnu2.model.entity.GradePost
import com.xhand.hnu2.model.entity.KbList
import com.xhand.hnu2.model.entity.KccjList
import com.xhand.hnu2.model.entity.LoginPostEntity
import com.xhand.hnu2.model.entity.SchedulePost
import com.xhand.hnu2.model.entity.Update
import com.xhand.hnu2.model.entity.UserInfoEntity
import com.xhand.hnu2.model.entity.Xscj
import com.xhand.hnu2.network.GradeService
import com.xhand.hnu2.network.LoginService
import com.xhand.hnu2.network.NewsDetailService
import com.xhand.hnu2.network.ScheduleService
import com.xhand.hnu2.network.UpdateService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async.Schedule


class SettingsViewModel : ViewModel() {

    var hasMessage by mutableStateOf(true)
    var checkboxes = mutableStateListOf(
        ToggleableInfo(
            isChecked = true,
            text = "今日课程",
            imageVector = Icons.Default.DateRange,
            route = "schedule_screen"
        ),
        ToggleableInfo(
            isChecked = true,
            text = "课程成绩",
            imageVector = Icons.Default.Edit,
            route = "grade_screen"
        )
    )

    // 今日课程
    var todaySchedule = mutableListOf<KbList>()

    // 成绩代码
    var cjdm by mutableStateOf("")

    // 成绩详情
    var gradeDetail: GradeInfo? = null
    var gradeDetails: Xscj? = null
    var gradeOrder: GradeInfo? = null

    // 新闻链接
    var url by mutableStateOf("")

    // 展示登录框
    var showPersonAlert by mutableStateOf(false)

    // TextFiled
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    // 登录信息
    var userInfo: UserInfoEntity? = null

    // 软件更新
    var ifUpdate by mutableStateOf(true) // 用户是否选择自动更新
    var ifNeedUpdate by mutableStateOf(false) // 是否有更新

    // 展示登录弹窗
    var isShowDialog by mutableStateOf(false)

    // 展示退出登录弹窗
    var isShowAlert by mutableStateOf(false)

    // 是否登录成功
    val isLoginSuccess: Boolean
        get() {
            return userInfo != null
        }

    // 是否正在登录
    var loginCircle by mutableStateOf(false)
    var isLogging by mutableStateOf(false)

    // 成绩列表
    var gradeList = mutableListOf<KccjList>()

    // 成绩列表——临时
    private var gradeListTemp = mutableListOf<KccjList>()

    // 是否正在刷新
    var isRefreshing by mutableStateOf(false)
        private set

    // 网页源码请求
    var htmlParsing: String = ""

    // 学号 年级
    private val grade: String
        get() {
            return userInfo?.studentID?.substring(0, 2) ?: "22"
        }

    private val gradeInt: Int
        get() {
            return grade.toInt()
        }

    // 学期
    val gradeTerm: MutableList<String>
        get() {
            return mutableListOf(
                "20${gradeInt}01",
                "20${gradeInt}02",
                "20${gradeInt + 1}01",
                "20${gradeInt + 1}02",
                "20${gradeInt + 2}01",
                "20${gradeInt + 2}02",
                "20${gradeInt + 3}01",
                "20${gradeInt + 3}02"
            )
        }

    // 网络请求
    private val gradeService = GradeService.instance()
    private val loginService = LoginService.instance()
    private val updateService = UpdateService.instance()
    private val detailService = NewsDetailService.instance()
    private val todayScheduleService = ScheduleService.instance()

    // 登录请求
    suspend fun login() {
        val publicKey = RSAEncryptionHelper.getPublicKeyFromString()
        val passwordEncrypt = RSAEncryptionHelper.encryptText(password, publicKey)
        val loginPost = LoginPostEntity(
            username = username,
            password = passwordEncrypt,
            code = "",
            appid = null
        )
        try {
            val res = loginService.loginPost(loginPost)
            loginCircle = true
            delay(1200)
            userInfo = if (res.code.toInt() == 200) {
                UserInfoEntity(
                    name = res.user!!.userxm,
                    studentID = res.user.userAccount,
                    academy = res.user.userdwmc,
                    token = res.user.token
                )
            } else {
                null
            }
            loginCircle = false
            isLogging = true
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    // 成绩请求
    fun gradeService() = viewModelScope.launch {
        for (term in gradeTerm) {
            isRefreshing = true
            val res = userInfo?.let {
                gradeService.gradePost(
                    GradePost(term),
                    token = it.token
                )
            }
            if (res != null) {
                if (res.code.toInt() == 200) {
                    gradeListTemp = (res.kccjList + gradeListTemp).toMutableList()
                } else {
                    Log.i("TAG666", "null")
                }
            }
            isRefreshing = false
        }
        gradeList = gradeListTemp
        gradeListTemp = mutableListOf() // 下拉刷新时置空
    }

    // 成绩请求
    suspend fun gradeDetailService() {
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
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }

    }


    // 今日课程
    suspend fun todaySchedule() {
        val res =
            userInfo?.let {
                todayScheduleService.scheduleDetail(
                    SchedulePost(todaykb = "1"),
                    token = it.token
                )
            }
        if (res != null) {
            if (res.code == 200) {
                todaySchedule =
                    res.kbList.sortedBy { it.qssj.substring(0, 2).toInt() }.toMutableList()
            }
        }
    }


    // 新闻页面详情请求
    suspend fun detailService() {
        htmlParsing = try {
            val res = detailService.getNewsDetail(url)
            res.body()?.string() ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    // 软件更新请求
    suspend fun updateRes(currentVersion: String) {
        val res: Update
        try {
            res = updateService.update()
            ifNeedUpdate = res.version != currentVersion
            Log.i("TAG666", "${res.version}${currentVersion}${ifNeedUpdate}")
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    // 复制内容到剪切板
    @SuppressLint("StaticFieldLeak")
    fun copyText(cbManager: ClipboardManager, text: String) {
        cbManager.setText(AnnotatedString(text))
    }
}


