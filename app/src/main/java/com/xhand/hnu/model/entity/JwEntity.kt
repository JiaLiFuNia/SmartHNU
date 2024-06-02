package com.xhand.hnu.model.entity

// 返回参数
data class LoginEntity(
    val msg: String,
    val code: Long,
    val user: UserEntity? = null,
)

// 返回token
data class UserEntity(
    val token: String,
    val userxm: String,
    val userdwmc: String,
    val userAccount: String
)

// 登录请求
data class LoginPostEntity(
    val username: String,
    val password: String,
    val code: String,
    val appid: Any?
)

// 成绩请求
data class GradePost(
    val xnxqdm: String
)


// 成绩详情
data class GradeDetailPost(
    val cjdm: String
)

// 成绩详情回传
data class GradeDetailEntity(
    val msg: String,
    val code: Int,
    val info1: GradeInfo,
    val info: GradeInfo
)

data class GradeInfo(
    val zcj: Double,
    val rs: String,
    val mc: String,
    val z1: String,
    val z2: String,
    val z3: String,
    val z4: String,
    val z5: String,
    val ywmc: String? = null,
    val lx: String,
    val pm: Int,
    val cjdm: String
)


// 成绩回传 - 排名
data class GradeEntity(
    val msg: String,
    val code: Long,
    val kccjList: List<KccjList>,
)

data class KccjList(
    val xsxm: String, // 姓名
    val zcjfs: Double, // 总成绩
    val xnxqmc: String, // 学期
    val kcflmc: String, // 博约
    val kcdlmc: String, // 课程类型
    val cjjd: Double, // 绩点
    val kcrwdm: String, // 课程代码
    val kcmc: String, // 课程名称
    val cjdm: String, // 成绩代码
    val xf: Long, // 学分
    val xnxqdm: String, // 学期
    val xdfsmc: String, // 类型
    val cjfsmc: String, // 成绩类型
    val zcj: String // 成绩
)


// 成绩回传 - 平时成绩
data class GradeDetailsEntity(
    val msg: String,
    val code: Int,
    val xscj: Xscj,
)

data class Xscj(
    val zcj: String, // 总成绩
    val bl1: Int? = 100, // 平时占比
    val cj1: Double? = 100.0, // 平时
    val bl4: Int? = 100, // 期末占比
    val cj4: Double? = 100.0, // 期末
)


// 今日任务
data class SchedulePost(
    val todaykb: String = "1"
)

data class ScheduleEntity(
    val msg: String,
    val code: Int,
    val kbList: List<KbList>,
)

data class KbList(
    val khfsmc: String, // 考试或考察
    val qssj: String, // 开始时间
    val jssj: String, // 结束时间
    val kcmc: String, // 课程名称
    val teaxms: String, // 教师姓名
    val jxcdmc: String, // 上课地点
    // val flfzmc: String,
    // val ps: String,
    // val pkrs: Long,
    // val sknrjj: String,
    // val lngandlat: String,
    // val zwh: Long,
    // val jxhjmc: String,
    // val lx: String,
    // val kcywmc: String,
    // val xnxqmc: String,
    // val jxbdm: String,
    // val qsrq: String,
    // val jxbrs: Long,
    // val bz: String,
    // val kxh: String,
    // val szxqmc: String,
    // val kcptbh: String,
    // val skrq: String,
    // val address: String,
    // val tasktype: Long,
    // val jxbmc: String,
    // val zc: String,
    // val jxxsmc: String,
    // val kcbh: String,
    // val bgcolor: String,
    // val jcdm: String,
    // val jzwmc: String,
    // val xmmc: String,
    // val jcdm2: String,
    // val pe: String,
    // val dgksdm: String,
    // val xqmc: String,
    // val jsrq: String,
    // val xq: String,
)


//"kbList": [
//    {
//      "flfzmc": "",
//      "khfsmc": "考试",
//      "ps": "07",
//      "pkrs": 105,
//      "sknrjj": "",
//      "lngandlat": "",
//      "zwh": 1,
//      "jxhjmc": "实践",
//      "qssj": "16:40:00",
//      "lx": "kb",
//      "kcywmc": "",
//      "xnxqmc": "2023-2024-2",
//      "jxbdm": "10057751",
//      "jssj": "18:20:00",
//      "qsrq": "2024-03-20",
//      "jxbrs": 93,
//      "bz": "0308--",
//      "kxh": "6",
//      "szxqmc": "校本部",
//      "kcmc": "数学学科课程教学论",
//      "kcptbh": "SX000800104",
//      "teaxms": "王允",
//      "skrq": "2024-03-20",
//      "address": "",
//      "tasktype": 1,
//      "jxbmc": "22信计班",
//      "zc": "4",
//      "jxxsmc": "线下教学",
//      "kcbh": "005319",
//      "jxcdmc": "启智楼308",
//      "bgcolor": "#AFAF61",
//      "jcdm": "0708",
//      "jzwmc": "启智楼",
//      "xmmc": "",
//      "jcdm2": "07,08",
//      "pe": "08",
//      "dgksdm": "13279168",
//      "xqmc": "校本部",
//      "jsrq": "2024-03-20",
//      "xq": "3"
//    },


data class teacherPost(
    val xnxqdm: String
)

data class TeacherEntity(
    val msg: String,
    val allPjxxList: List<AllPjxxList>,
    val code: Long,
    val xnxqdm: String,
    val xnxqmc: String,
)

data class AllPjxxList(
    val teadm: String,
    val jxhjmc: String,
    val xnxqmc: String,
    val xnxqdm: String,
    val dgksdm: String,
    val teaxm: String,
    val kcmc: String,
)

