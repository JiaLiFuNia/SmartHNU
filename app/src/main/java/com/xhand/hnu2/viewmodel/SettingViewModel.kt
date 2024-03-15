package com.xhand.hnu2.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu2.R
import com.xhand.hnu2.components.RSAEncryptionHelper
import com.xhand.hnu2.model.entity.GradePost
import com.xhand.hnu2.model.entity.KccjList
import com.xhand.hnu2.model.entity.LoginPostEntity
import com.xhand.hnu2.model.entity.Update
import com.xhand.hnu2.model.entity.UserInfoEntity
import com.xhand.hnu2.network.GradeService
import com.xhand.hnu2.network.LoginService
import com.xhand.hnu2.network.NewsDetailService
import com.xhand.hnu2.network.UpdateService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SettingsViewModel : ViewModel() {
    var url by mutableStateOf("")

    // 展示登录框
    var showPersonAlert by mutableStateOf(false)

    // TextFiled
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    // 登录信息
    var userInfo: UserInfoEntity? = null

    // 软件更新
    val ifUpdate: Boolean = true

    // 展示登录弹窗
    var isShowDialog by mutableStateOf(false)

    // 展示退出弹窗
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

    // 学期
    private val gradeTerm =
        mutableListOf(
            "202201",
            "202202",
            "202301",
            "202302",
            "202401",
            "202402",
            "202501",
            "202502"
        )

    // 网络请求
    private val gradeService = GradeService.instance()
    private val loginService = LoginService.instance()
    private val updateService = UpdateService.instance()
    private val detailService = NewsDetailService.instance()

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
            Log.i("TAG666", "$res")
            loginCircle = false
            isLogging = true
        } catch (e: Exception) {
            Log.i("TAG666", e.toString())
        }

    }

    // 成绩请求
    fun gradeService() = viewModelScope.launch {
        for (term in gradeTerm) {
            isRefreshing = true
            Log.i("TAG666", "667$gradeList")
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
            Log.i("TAG666", "66$gradeList")
            isRefreshing = false
        }
        gradeList = gradeListTemp
        gradeListTemp = mutableListOf() // 下拉刷新时置空
    }

    // 新闻页面源码请求
    suspend fun detailService() {
        try {
            val res = detailService.getNewsDetail(url)
            htmlParsing = res.body()?.string() ?: ""
            Log.i("TAG666", htmlParsing)
        } catch (e: Exception) {
            htmlParsing = ""
        }

    }

    @SuppressLint("StaticFieldLeak")
    fun copyText(cbManager: ClipboardManager, context: Context) {
        cbManager.setText(AnnotatedString(context.getString(R.string.qq_group_number)))
    }

    suspend fun updateRes(): Update {
        return updateService.update()
    }
}