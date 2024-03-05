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
import com.xhand.hnu2.R
import com.xhand.hnu2.components.RSAEncryptionHelper
import com.xhand.hnu2.model.entity.LoginPostEntity
import com.xhand.hnu2.model.entity.Update
import com.xhand.hnu2.model.entity.UserInfoEntity
import com.xhand.hnu2.network.LoginService
import com.xhand.hnu2.network.UpdateService
import kotlinx.coroutines.delay


class SettingsViewModel() : ViewModel() {
    // private val userInfoManager = UserInfoManager(context = context)
    /*init {
        viewModelScope.launch {
            val name = userInfoManager.userName.firstOrNull()
            val stuid = userInfoManager.stuID.firstOrNull()
            val academic = userInfoManager.accadamic.firstOrNull()
            userInfo = if (name?.isNotEmpty() == true) {
                UserInfoEntity(
                    name = name,
                    studentID = if (stuid?.isNotEmpty() == true) stuid else "",
                    academy = if (academic?.isNotEmpty() == true) academic else ""
                )
            } else {
                null
            }
        }
    }*/
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var userInfo: UserInfoEntity? = null
    val ifUpdate: Boolean = true
    var isShowDialog by mutableStateOf(false)
    var isShowAlert by mutableStateOf(false)
    var isLoginSuccess by mutableStateOf(false)
    var isLogining by mutableStateOf(false)

    private val loginService = LoginService.instance()
    private val updateService = UpdateService.instance()

    // 登录是否成功
    val logined: Boolean
        get() {
            return userInfo != null
        }

    // 登录请求
    suspend fun login() {
        val publicKey = RSAEncryptionHelper.getPublicKeyFromString()
        val passwordEncrypt = publicKey?.let { RSAEncryptionHelper.encryptText(password, it) }
        val loginPost = passwordEncrypt?.let {
            LoginPostEntity(
                username = username,
                password = it,
                code = "",
                appid = null
            )
        }
        val res = loginPost?.let { loginService.loginPost(it) }
        isLogining = true
        delay(2000)
        if (res != null) {
            if (res.code() == 200) {
                Log.i("TAG666", "${res.body()}")
                userInfo = res.body()?.user?.let {
                    UserInfoEntity(
                        name = it.userxm,
                        studentID = res.body()?.user!!.userAccount,
                        academy = res.body()?.user!!.userdwmc
                    )
                }
                isLoginSuccess = true
                Log.i("TAG666", "userInfo is not null")
            } else {
                userInfo = null
                isLoginSuccess = false
                Log.i("TAG666", "userInfo is null")
            }
        }
        isLogining = false
        Log.i("TAG666", "$userInfo")
    }



    @SuppressLint("StaticFieldLeak")
    fun copyText(cbManager: ClipboardManager, context: Context) {
        cbManager.setText(AnnotatedString(context.getString(R.string.qq_group_number)))
    }

    suspend fun updateRes(): Update {
        return updateService.update()
    }
}