package com.smart.htu.screens.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Destinations(
    val route: String
) {
    data object App : Destinations("main")
    data object Login : Destinations("login")
    data object News : Destinations("news")
    data object Person : Destinations("person")
    data object Application : Destinations("application")
    data object NewsSearch : Destinations("news_search")
    data object NewsHistory : Destinations("news_history")
    data object NewsDetail : Destinations("news_detail")
    data object ApplicationEdit : Destinations("application_edit")
    data object Message : Destinations("message")
    data object Setting : Destinations("setting")
    data object About : Destinations("about")
    data object ClassroomSearch : Destinations("classroom_search")
    data object ApplicationWebView : Destinations("application_webview")
    data object License : Destinations("license")
    data object LibrarySearch : Destinations("library_search")
    data object LibrarySearchDetail : Destinations("library_search_detail")
    data object AirCondition : Destinations("air_condition")
    data object AirConditionSetting : Destinations("air_condition_setting")
    data object AccountManage : Destinations("account_manage")
    data object Grade : Destinations("grade")
    data object TeacherEvaluation : Destinations("teacher_evaluation")
    data object TeacherEvaluationDetail : Destinations("teacher_evaluation_detail")
    data object Textbook : Destinations("textbook")
    data object TextbookSelect : Destinations("textbook_select")
    data object CourseTable : Destinations("course_table")
    data object Feedback : Destinations("feedback")
    data object WebsiteNavigation : Destinations("website_navigation")
    data object AIConfiguration : Destinations("ai_configuration")
    data object PhysicalTest : Destinations("physical_test")
    data object PdfReaderView : Destinations("pdf_viewer")
    data object MessageBoard : Destinations("message_board")
    data object MessageBoardDetail : Destinations("message_board_detail")
    data object CampusLife : Destinations("campus_life")
    data object ArticleStyle : Destinations("article_style")
    data object ExamSchedule: Destinations("exam_schedule")
    data object AddExamSchedule: Destinations("add_exam_schedule")
}

@Serializable
data object MainFrame : NavKey

@Serializable
data object Login : NavKey

@Serializable
data object News : NavKey

@Serializable
data object Person : NavKey

@Serializable
data object Application : NavKey

@Serializable
data object NewsSearch : NavKey

@Serializable
data object NewsHistory : NavKey

@Serializable
data class NewsDetail(
    val url: String,
    val title: String
) : NavKey

@Serializable
data object ApplicationEdit : NavKey

@Serializable
data object Message : NavKey

@Serializable
data object Setting : NavKey

@Serializable
data object About : NavKey

@Serializable
data object ClassroomSearch : NavKey

@Serializable
data class SharedWebView(
    val url: String,
    val label: String
) : NavKey

@Serializable
data object License : NavKey

@Serializable
data object LibrarySearch : NavKey

@Serializable
data class LibrarySearchDetail(val bookId: String) : NavKey

@Serializable
data object AirCondition : NavKey

@Serializable
data object AirConditionSetting : NavKey

@Serializable
data object AccountManage : NavKey

@Serializable
data object Grade : NavKey

@Serializable
data object TeacherEvaluation : NavKey

@Serializable
data class TeacherEvaluationDetail(
    val syllabusEvaluateCode: String,
    val teacherCode: String
) : NavKey

@Serializable
data object Textbook : NavKey

@Serializable
data class TextbookSelect(
    val courseTaskCode: String,
    val termCode: String
) : NavKey

@Serializable
data object CourseTable : NavKey

@Serializable
data object Feedback : NavKey

@Serializable
data object WebsiteNavigation : NavKey

@Serializable
data object AIConfiguration : NavKey

@Serializable
data object PhysicalTest : NavKey

@Serializable
data class PdfReaderView(
    val url: String,
    val title: String
) : NavKey

@Serializable
data object MessageBoard : NavKey