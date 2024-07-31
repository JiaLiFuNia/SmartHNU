package com.xhand.hnu.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.R
import com.xhand.hnu.components.RSAEncryptionHelper
import com.xhand.hnu.model.UserInfoManager
import com.xhand.hnu.model.entity.AllPjxxList
import com.xhand.hnu.model.entity.BookDetailPost
import com.xhand.hnu.model.entity.BookPost
import com.xhand.hnu.model.entity.CheckTokenEntity
import com.xhand.hnu.model.entity.ClassroomPost
import com.xhand.hnu.model.entity.GradeDetailPost
import com.xhand.hnu.model.entity.GradeInfo
import com.xhand.hnu.model.entity.GradePost
import com.xhand.hnu.model.entity.HourListEntity
import com.xhand.hnu.model.entity.JDList
import com.xhand.hnu.model.entity.JDPost
import com.xhand.hnu.model.entity.Jszylist
import com.xhand.hnu.model.entity.Jxllist
import com.xhand.hnu.model.entity.KbList
import com.xhand.hnu.model.entity.KccjList
import com.xhand.hnu.model.entity.Kxjcdata
import com.xhand.hnu.model.entity.LoginPostEntity
import com.xhand.hnu.model.entity.MessageDetail
import com.xhand.hnu.model.entity.MessagePost
import com.xhand.hnu.model.entity.ReadMessagePost
import com.xhand.hnu.model.entity.SchedulePost
import com.xhand.hnu.model.entity.SecondClassInfo
import com.xhand.hnu.model.entity.Update
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.model.entity.Xdjcdata
import com.xhand.hnu.model.entity.Xscj
import com.xhand.hnu.model.entity.Yxjcdata
import com.xhand.hnu.model.entity.teacherPost
import com.xhand.hnu.network.GradeService
import com.xhand.hnu.network.LoginService
import com.xhand.hnu.network.NewsDetailService
import com.xhand.hnu.network.ScheduleService
import com.xhand.hnu.network.SecondClassService
import com.xhand.hnu.network.UpdateService
import com.xhand.hnu.network.secondClassLoginState
import com.xhand.hnu.network.secondClassParsing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@SuppressLint("MutableCollectionMutableState")
class SettingsViewModel(context: Context) : ViewModel() {
    private val userInfoManager = UserInfoManager(context)
    init {
        viewModelScope.launch {
            val isLogged = userInfoManager.logged.firstOrNull()
            val userInfoStore = userInfoManager.userInfo.firstOrNull()
            val logInfo = userInfoManager.logInfo.firstOrNull()
            val scUserInfo = userInfoManager.scUserInfo.firstOrNull()
            if (scUserInfo != null) {
                username = scUserInfo.username
                scPassword = scUserInfo.password
                cookie = scUserInfo.cookie
                stateCode = 1
            }
            userInfo = userInfoStore
            loginCode = isLogged ?: 0
            if (logInfo != null) {
                password = logInfo.password
                username = logInfo.username
            }
        }
    }
    var isDynamicColor by mutableStateOf(true)
    var darkModeIndex by mutableIntStateOf(0)
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
        ),
        ToggleableInfo(
            isChecked = true,
            text = "第二课堂",
            imageVector = Icons.Default.Star,
            route = "teacher_screen"
        ),
    )
    var functionCards = mutableStateListOf(
        FunctionCard(
            title = "教室查询",
            painterResource = R.drawable.ic_baseline_location_city_24,
            route = "classroom_screen"
        ),
        FunctionCard(
            title = "选订教材",
            painterResource = R.drawable.outline_menu_book_24,
            route = "search_screen"
        ),
        FunctionCard(
            title = "上课任务",
            painterResource = R.drawable.ic_baseline_task_24,
            route = "task_screen"
        ),
        FunctionCard(
            title = "课程查询",
            painterResource = R.drawable.ic_outline_manage_search_24,
            route = "courseSearch_screen"
        ),
        FunctionCard(
            title = "教学评价",
            painterResource = R.drawable.outline_assessment_24,
            route = "teacher_screen"
        )
    )

    // 今日课程
    var todaySchedule by mutableStateOf(mutableListOf<KbList>())

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

    // 展示登录框
    var showPersonAlert by mutableStateOf(false)
    var showBookAlert by mutableStateOf(false)
    var showBookSelect by mutableStateOf(false)

    // 展示信息详情
    var showMessageDetail by mutableStateOf(false)
    var showHaveReadAlert by mutableStateOf(false)

    // TextFiled
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var scPassword by mutableStateOf("")

    // 登录信息
    var userInfo: UserInfoEntity? = null

    // 软件更新
    var ifNeedUpdate by mutableStateOf(false) // 是否有更新

    // 展示登录弹窗
    var isShowDialog by mutableStateOf(false)
    var isShowScDialog by mutableStateOf(false)

    // 展示退出登录弹窗
    var isShowAlert by mutableStateOf(false)

    // 是否登录成功
    val isLoginSuccess: Boolean
        get() {
            return loginCode == 200
        }

    // 是否正在登录
    var loginCircle by mutableStateOf(false)
    var loginCode by mutableIntStateOf(0)

    var isGettingDetailGrade by mutableStateOf(true)
    var isGettingCourse by mutableStateOf(true)
    var isGettingGrade by mutableStateOf(true)

    // 是否有消息
    var hasMessage by mutableStateOf(mutableListOf<MessageDetail>())
    private var bulidingsData = mutableListOf<Jxllist>()
    // var holidayData = mutableStateOf(HolidayEntity(-1, Type(0, "", 0), ""))

    // 成绩列表
    var gradeList = mutableListOf<KccjList>()
    var teacherList by mutableStateOf(mutableListOf<AllPjxxList>())
    var jdList = mutableListOf<JDList>()

    // 是否正在刷新

    // 网页源码请求
    var htmlParsing by mutableStateOf("")

    // 学号 年级
    private val grade: String
        get() {
            return userInfo?.studentID?.substring(0, 2) ?: "22"
        }

    private val gradeInt: Int
        get() {
            return grade.toInt()
        }

    val longGradeTerm: MutableList<String>
        get() {
            return mutableListOf(
                "20${gradeInt}-20${gradeInt + 1}-1",
                "20${gradeInt}-20${gradeInt + 1}-2",
                "20${gradeInt + 1}-20${gradeInt + 2}-1",
                "20${gradeInt + 1}-20${gradeInt + 2}-2",
                "20${gradeInt + 2}-20${gradeInt + 3}-1",
                "20${gradeInt + 2}-20${gradeInt + 3}-2",
                "20${gradeInt + 3}-20${gradeInt + 4}-1",
                "20${gradeInt + 3}-20${gradeInt + 4}-2"
            )
        }

    // 学期
    val gradeTerm: MutableList<String>
        get() {
            return mutableListOf(
                "20${gradeInt + 3}02",
                "20${gradeInt + 3}01",
                "20${gradeInt + 2}02",
                "20${gradeInt + 2}01",
                "20${gradeInt + 1}02",
                "20${gradeInt + 1}01",
                "20${gradeInt}02",
                "20${gradeInt}01",
            )
        }

    // 网络请求
    private val gradeService = GradeService.instance()
    private val loginService = LoginService.instance()
    private val updateService = UpdateService.instance()
    private val detailService = NewsDetailService.instance()
    private val todayScheduleService = ScheduleService.instance()
    // private val holidayService = HolidayService.instance()

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
            loginCircle = true
            val res = loginService.loginPost(loginPost)
            Log.i("TAG666", "${res}")
            delay(1200)
            userInfo = if (res.code == 200) {
                UserInfoEntity(
                    name = res.user!!.userxm,
                    studentID = res.user.userAccount,
                    academy = res.user.userdwmc,
                    token = res.user.token
                )
            } else {
                null
            }
            val logInfo = LoginPostEntity(
                username = username,
                password = password,
                code = "",
                appid = null
            )
            viewModelScope.launch {
                userInfo?.let { userInfoManager.save(it, res.code, logInfo) }
            }
            loginCircle = false
            loginCode = res.code
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    fun checkToken() = viewModelScope.launch {
        val token = userInfo?.token ?: ""
        var res: CheckTokenEntity? = null
        if (loginCode != 0) {
            res = gradeService.checkToken(token)
        }
        if (res != null) {
            if (res.code != 200) {
                if (password.isNotEmpty() and username.isNotEmpty()) {
                    login()
                }
            }
        }
    }

    fun clearUserInfo() {
        viewModelScope.launch {
            userInfoManager.clear()
            stateCode = 0
            userInfo = null
            loginCode = 0
        }
    }

    // 成绩请求
    suspend fun gradeService() {
        gradeList.clear() // 下拉刷新时置空
        var order = 0
        for (term in gradeTerm) {
            val res = userInfo?.let {
                gradeService.gradePost(
                    GradePost(term),
                    token = it.token
                )
            }
            if (res != null) {
                if (res.code.toInt() == 200) {
                    for (i in res.kccjList) {
                        order += 1
                        i.order = order
                    }
                    gradeList.addAll(res.kccjList)
                    Log.i("TAG666", gradeList.toString())
                } else {
                    Log.i("TAG666", "null")
                }
            }
        }
    }

    // 成绩请求
    suspend fun gradeDetailService(cjdm: String) {
        try {
            isGettingDetailGrade = true
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
            Log.i("TAG666", "$e")
        }
    }

    var isGettingJD by mutableStateOf(true)
    suspend fun JDService() {
        try {
            val res = userInfo?.let { gradeService.gradeJDDetail(JDPost("1", "01"), it.token) }
            if (res != null) {
                if (res.code == 200) {
                    jdList = res.list.toMutableList()
                }
            }
            isGettingJD = false
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

    var isGettingTeacher by mutableStateOf(true)
    var pjSelectTerm by mutableIntStateOf(3)
    // 教师评价
    suspend fun teacherService(xnxqdm: String) {
        try {
            val res =
                userInfo?.let {
                    gradeService.teacherDetails(
                        teacherPost(longToShort(xnxqdm)),
                        token = it.token
                    )
                }
            if (res != null) {
                teacherList = res.allPjxxList.toMutableList()
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
        Log.i("TAG6654",longToShort(xnxqdm))
        Log.i("TAG6654", "$teacherList")
    }
    suspend fun messageService() {
        try {
            val res = userInfo?.let {
                gradeService.messageDetails(
                    MessagePost(1, 1000000),
                    token = it.token
                )
            }
            if (res != null) {
                Log.i("TAG667", "$res")
                if (res.code == 200) {
                    hasMessage = res.data.toMutableList()
                }
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }


    fun readMessage(xxids: String) = viewModelScope.launch {
        try {
            val res = gradeService.readMessage(
                ReadMessagePost(xxids),
                token = userInfo?.token ?: ""
            )
            if (res.code == 200 && res.msg.contains("success")) {
                messageService()
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    suspend fun buildingService() {
        try {
            val res = userInfo?.let { gradeService.buildingData(token = it.token) }
            if (res != null) {
                Log.i("TAG667", "$res")
                if (res.code == 200)
                    bulidingsData = res.jxllist.toMutableList()
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    var haveClassRoom = mutableListOf<Jszylist>()
    var allClassRoom = mutableListOf<Jszylist>()
    var isGettingRoom by mutableStateOf(true)
    val buildingsSave = listOf(
        ClassroomPost("104", "启智楼", ""),
        ClassroomPost("107", "新五五四楼", ""),
        // ClassroomPost("119", "新联楼", currentDate),
        // ClassroomPost("301", "求是中楼（东区B楼）", currentDate),
        // ClassroomPost("302", "求是西楼（东区A楼）", currentDate),
        // ClassroomPost("307", "求是东楼", currentDate),
        ClassroomPost("102", "文渊楼", ""),
        // ClassroomPost("312", "综合实训楼", currentDate),
        ClassroomPost("310", "文昌楼（东综）", "")
    )

    suspend fun classroomService(date: String) {
        haveClassRoom.clear()
        allClassRoom.clear()
        try {
            for (i in buildingsSave) {
                i.rq = date
                val res = userInfo?.let {
                    gradeService.classroomData(i, token = it.token)
                }
                if (res != null) {
                    Log.i("TAG667", "$res")
                    if (res.code == 200) {
                        for (j in res.jxcdxxList) {
                            j.jzwdm = res.jzwdm
                        }
                        for (k in res.jszylist) {
                            k.jzwdm = res.jzwdm
                        }
                        haveClassRoom.addAll(res.jszylist)
                        allClassRoom.addAll(res.jxcdxxList)
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("TAG667", "$e")
        }
        isGettingRoom = false
    }

    var booksList = mutableListOf<Xdjcdata>()
    var isGettingBook by mutableStateOf(true)
    var selectTerm by mutableIntStateOf(4)
    suspend fun bookService(xnxqdm: String) {
        try {
            val res =
                userInfo?.let { gradeService.bookDetail(BookPost("", longToShort(xnxqdm)), token = it.token) }
            if (res != null) {
                Log.i("TAG667", "$res")
                if (res.code == 200)
                    booksList = res.xdjcdatas.toMutableList()
                Log.i("TAG667", "$booksList")
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    // 获取可选
    var bookAbleList by mutableStateOf(mutableListOf<Kxjcdata>())
    var isGettingBookDetail by mutableStateOf(true)
    suspend fun bookDetailService(kcrwdm: String, xnxqdm: String) {
        try {
            isGettingBookDetail = true
            val res = userInfo?.let {
                gradeService.bookDetail2(
                    BookDetailPost(kcrwdm, xnxqdm),
                    token = it.token
                )
            }
            if (res != null) {
                Log.i("TAG667", "$res")
                if (res.code == 200)
                    bookAbleList = res.kxjcdatas.toMutableList()
                Log.i("TAG667", "$bookAbleList")
            }
            isGettingBookDetail = false
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    // 获取已选
    var bookSelectedList by mutableStateOf(mutableListOf<Yxjcdata>())
    var isGettingBookSelected by mutableStateOf(true)
    suspend fun bookSelectedService(kcrwdm: String, xnxqdm: String) {
        try {
            isGettingBookSelected = true
            val res = userInfo?.let {
                gradeService.bookDetail3(
                    BookDetailPost(kcrwdm, xnxqdm),
                    token = it.token
                )
            }
            if (res != null) {
                Log.i("TAG667", "$res")
                if (res.code == 200)
                    bookSelectedList = res.yxjcdatas.toMutableList()
                Log.i("TAG667", "$bookSelectedList")
            }
            isGettingBookSelected = false
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    fun getCurrentDates(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(formatter)
        return formattedDate
    }
    /*suspend fun holidayService() {
        try {
            val res = holidayService.holiday()
            Log.i("TAG667", "$res")
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }*/


    var updateMessage by mutableStateOf(Update("", "", "",""))
    var isShowUpdateDialog by mutableStateOf(false)
    var isGettingUpdate by mutableStateOf(false)
    // 软件更新请求
    suspend fun updateService(currentVersion: String) {
        try {
            isGettingUpdate = true
            updateMessage = updateService.update()
            ifNeedUpdate = convertVersion(updateMessage.version) > convertVersion(currentVersion)
            isGettingUpdate = false
            Log.i("TAG666", "${updateMessage.version}${convertVersion(currentVersion)}${ifNeedUpdate}")
        } catch (e: Exception) {
            isGettingUpdate = false
            Log.i("TAG666", "$e")
        }
    }
    fun updateRes(currentVersion: String) = viewModelScope.launch {
        updateService(currentVersion)
    }

    private fun convertVersion(version: String): Int {
        val versionInt = version.split('.').joinToString("")
        return versionInt.toInt()
    }


    // 第二课堂
    private val login = SecondClassService.instance(context)
    private var secondClassHtml by mutableStateOf("")
    private var scToken by mutableStateOf("")
    var verifyImg by mutableStateOf("http://dekt.htu.edu.cn/img/resources-code.jpg")
    var scLoginCircle by mutableStateOf(false) // 是否正在登录
    var verifycode by mutableStateOf("") // 验证码
    var cookie by mutableStateOf("") // cookie
    var stateCode by mutableIntStateOf(0) // 登录状态码
    suspend fun secondClassService() {
        try {
            val res = login.scLoginService()
            secondClassHtml = res.body()?.string() ?: ""
            scToken = secondClassParsing(secondClassHtml)
            cookie = try {
                res.raw().header("Set-Cookie").toString().substring(4, 40)
            } catch (e: Exception) {
                ""
            }
            Log.i("TAG666", cookie)
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    suspend fun secondClassLogin() {
        try {
            scLoginCircle = true // 开始加载
            val publicKey = RSAEncryptionHelper.getScPublicKeyFromString()
            val passwordEncrypt = RSAEncryptionHelper.encryptText(scPassword, publicKey)
            val res = login.scLoginPostService(
                cookie = "sid=${cookie}",
                username = username,
                password = "",
                password2 = passwordEncrypt,
                tk = scToken,
                verifycode = verifycode
            )
            val resString = res.body()?.string()
            delay(1500)
            scToken = resString?.let { secondClassParsing(it) }.toString() // 更新tk
            stateCode = resString?.let { secondClassLoginState(it) }!! // 更新状态码
            if (stateCode == -2 || stateCode == -3) {
                cookie = try {
                    res.raw().header("Set-Cookie").toString().substring(4, 40)
                } catch (e: Exception) {
                    ""
                }
                verifycode = ""
            }
            Log.i("TAG666", "stateCode: $stateCode")
            scLoginCircle = false // 结束加载
            if (stateCode == 1) {
                val scUserInfo = SecondClassInfo(
                    username = username,
                    password = scPassword,
                    cookie = cookie,
                )
                viewModelScope.launch {
                    userInfoManager.saveSecondClassInfo(scUserInfo)
                }
            }
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
            scLoginCircle = false // 如果出错同样结束加载
        }
    }

    var scHourList by mutableStateOf(mutableListOf<HourListEntity>())
    var isGettingHourList by mutableStateOf(true)
    suspend fun getHourList() {
        try {
            scHourList = login.getHourList(cookie = "sid=${cookie}").toMutableList()
            isGettingHourList = false
            Log.i("TAG6663", "$scHourList")
        } catch (e: Exception) {
            isGettingHourList = false
            stateCode = 0
            Log.i("TAG6663", "$e")
        }
    }

    // 复制内容到剪切板
    @SuppressLint("StaticFieldLeak")
    fun copyText(cbManager: ClipboardManager, text: String) {
        cbManager.setText(AnnotatedString(text))
    }

    // 学期转换
    fun longToShort(xq: String): String {
        return if (xq.length == 6) {
            "${xq.substring(0, 4)}-${xq.substring(0, 4).toInt() + 1}-${xq.last()}"
        } else
            "${xq.substring(0, 4)}0${xq.last()}"
    }
}


