package com.xhand.hnu.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.xhand.hnu.R
import com.xhand.hnu.components.RSAEncryptionHelper
import com.xhand.hnu.model.entity.AllPjxxList
import com.xhand.hnu.model.entity.BuildingEntiy
import com.xhand.hnu.model.entity.ClassroomPost
import com.xhand.hnu.model.entity.GradeDetailPost
import com.xhand.hnu.model.entity.GradeInfo
import com.xhand.hnu.model.entity.GradePost
import com.xhand.hnu.model.entity.HolidayEntity
import com.xhand.hnu.model.entity.Jszylist
import com.xhand.hnu.model.entity.JxcdxxList
import com.xhand.hnu.model.entity.Jxllist
import com.xhand.hnu.model.entity.KbList
import com.xhand.hnu.model.entity.KccjList
import com.xhand.hnu.model.entity.LoginPostEntity
import com.xhand.hnu.model.entity.MessageDetail
import com.xhand.hnu.model.entity.MessagePost
import com.xhand.hnu.model.entity.SchedulePost
import com.xhand.hnu.model.entity.Type
import com.xhand.hnu.model.entity.Update
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.model.entity.Xscj
import com.xhand.hnu.model.entity.teacherPost
import com.xhand.hnu.network.GradeService
import com.xhand.hnu.network.HolidayService
import com.xhand.hnu.network.LoginService
import com.xhand.hnu.network.NewsDetailService
import com.xhand.hnu.network.ScheduleService
import com.xhand.hnu.network.UpdateService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SettingsViewModel : ViewModel() {

    var readNotice by mutableStateOf(false)
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
    var functionCards = mutableStateListOf(
        FunctionCard(
            title = "教室查询",
            painterResource = R.drawable.ic_baseline_location_city_24,
            route = "classroom_screen"
        ),
        FunctionCard(
            title = "课程查询",
            painterResource = R.drawable.ic_outline_manage_search_24,
            route = "search_screen"
        ),
        FunctionCard(
            title = "上课任务",
            painterResource = R.drawable.ic_baseline_task_24,
            route = "task_screen"
        ),
        FunctionCard(
            title = "学习计划",
            painterResource = R.drawable.ic_outline_schedule_24,
            route = "plan_screen"
        ),
        FunctionCard(
            title = "教学评价",
            painterResource = R.drawable.ic_baseline_menu_book_24,
            route = "teacher_screen"
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

    // 展示信息详情
    var showMessageDetail by mutableStateOf(false)

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
            return loginCode == 200
        }

    // 是否正在登录
    var loginCircle by mutableStateOf(false)
    var loginCode by mutableIntStateOf(0)
    var isGettingGrade by mutableStateOf(true)
    var isGettingCourse by mutableStateOf(true)

    // 是否有消息
    var hasMessage = mutableListOf<MessageDetail>()
    var bulidingsData = mutableListOf<Jxllist>()
    // var holidayData = mutableStateOf(HolidayEntity(-1, Type(0, "", 0), ""))

    // 成绩列表
    var gradeList = mutableListOf<KccjList>()
    var teacherList = mutableListOf<AllPjxxList>()

    // 成绩列表——临时
    private var gradeListTemp = mutableListOf<KccjList>()
    private var teacherListTemp = mutableListOf<AllPjxxList>()

    // 是否正在刷新
    var isRefreshing by mutableStateOf(false)
        private set

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
            loginCode = res.code.toInt()
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
            isGettingGrade = true
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
            isGettingGrade = false
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
        isGettingCourse = false
    }

    // 教师评价
    suspend fun teacherService() {
        val res =
            userInfo?.let { gradeService.teacherDetails(teacherPost("202302"), token = it.token) }
        if (res != null) {
            teacherListTemp = res.allPjxxList.toMutableList()
        }
        teacherList = teacherListTemp
        Log.i("TAG6654", "$teacherList")
    }

    var isDetailLoad by mutableStateOf(true)

    // 新闻页面详情请求
    suspend fun detailService() {
        htmlParsing = try {
            val res = detailService.getNewsDetail(url)
            res.body()?.string() ?: ""
        } catch (e: Exception) {
            ""
        }
        isDetailLoad = false
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
    var allClassRoom = mutableListOf<JxcdxxList>()
    var isGettingRoom by mutableStateOf(true)
    private val currentDate = getCurrentDate()
    val buildingsSave = listOf(
        ClassroomPost("104", "启智楼", currentDate),
        ClassroomPost("107", "新五五四楼", currentDate),
        ClassroomPost("119", "新联楼", currentDate),
        ClassroomPost("301", "求是中楼（东区B楼）", currentDate),
        ClassroomPost("302", "求是西楼（东区A楼）", currentDate),
        ClassroomPost("307", "求是东楼", currentDate),
        ClassroomPost("102", "文渊楼", currentDate),
        ClassroomPost("312", "综合实训楼", currentDate),
        ClassroomPost("310", "文昌楼（东综）", currentDate)
    )

    suspend fun classroomService() {
        haveClassRoom.clear()
        allClassRoom.clear()
        try {
            for (i in buildingsSave) {
                val res = userInfo?.let {
                    gradeService.classroomData(i, token = it.token)
                }
                if (res != null) {
                    Log.i("TAG667", "$res")
                    if (res.code == 200) {
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

    private fun getCurrentDate(): String {
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


