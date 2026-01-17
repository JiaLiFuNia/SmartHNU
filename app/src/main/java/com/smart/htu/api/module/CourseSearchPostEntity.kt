package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import com.smart.htu.utils.TermUtil.getCurrentTerm
import kotlinx.serialization.Serializable

data class CourseSearchIndex(
    val msg: String,
    val code: Int,
    @SerializedName("xnxqdm") val termCode: String,                     // 学年学期代码
    @SerializedName("xnxqmc") val termString: String,                     // 学年学期名称
    @SerializedName("xnxqList") val termList: List<SingleTerm>,          // 学年学期列表
    @SerializedName("xqList") val campusList: List<OptionItem>,         // 校区列表
    @SerializedName("jxlList") val buildingList: List<OptionItem>,      // 教学楼列表
    @SerializedName("gnqList") val functionalAreaList: List<OptionItem>,// 功能区列表
    @SerializedName("zyList") val majorList: List<OptionItem>,          // 专业列表
    @SerializedName("kkyxList") val departmentList: List<OptionItem>,   // 开课院系列表
    @SerializedName("xsyxList") val studentDepartmentList: List<OptionItem>, // 学生院系列表
    @SerializedName("kkjysList") val teachingOfficeList: List<Any?>,    // 开课教研室列表
    @SerializedName("xsnjList") val studentGradeList: List<OptionItem>, // 学生年级列表
    @SerializedName("jhlxList") val planTypeList: List<OptionItem>      // 计划类型列表
) {
    data class OptionItem(
        val title: String,
        val value: String
    )
}


@Serializable
data class CourseSearchPostEntity(
    val pageNumber: Int = 1,
    val pageSize: Int = 50,
    @SerializedName("xnxqdm") val termCode: String = getCurrentTerm(),      // 学年学期代码
    @SerializedName("xqdm") val campusCode: String? = "",                 // 校区代码
    @SerializedName("zydm") val majorCode: String? = "",                  // 专业代码
    @SerializedName("kcmc") val courseName: String? = "",                 // 课程名称
    @SerializedName("teaxm") val teacherName: String? = "",               // 教师姓名
    @SerializedName("jzwdm") val buildingCode: String? = "",              // 建筑物代码
    @SerializedName("rq") val date: String? = "",                                 // 日期
    @SerializedName("zc") val week: String? = "",                         // 周次
    @SerializedName("xq") val dayOfWeek: String? = "",                    // 星期
    @SerializedName("jcdm") val sessionCode: String? = "",                // 节次代码
    @SerializedName("kkyxdm") val departmentCode: String? = "",           // 开课院系代码
    @SerializedName("xsyxdm") val studentDepartmentCode: String? = "",    // 学生院系代码
    @SerializedName("xsnj") val studentGrade: String? = "",               // 学生年级
    @SerializedName("jhlxdm") val planTypeCode: String? = "",             // 计划类型代码
    @SerializedName("jxcdmc") val teachingVenueName: String? = "",        // 教学场地名称

    @SerializedName("kkjysdm") val teachingOfficeCode: String? = "",      // 开课教研室代码
    @SerializedName("kcywmc") val courseEnglishName: String? = "",           // 课程英文名称
    @SerializedName("jxbmc") val teachingClassName: String? = "",         // 教学班名称
    @SerializedName("gnqdm") val functionalAreaCode: String? = "",        // 功能区代码
)

data class CourseSearchRes(
    val msg: String,
    val code: Int,
    @SerializedName("kbList") val courseInfoList: List<CourseInfoEntity>
)