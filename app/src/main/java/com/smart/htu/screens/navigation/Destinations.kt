package com.smart.htu.screens.navigation

sealed class Destinations(
    val route: String
) {
    data object App : Destinations("main")
    data object Login : Destinations("login")
    data object News : Destinations("news")
    data object Person : Destinations("person")
    data object Application : Destinations("application")
    data object NewsSearch : Destinations("news_search")
    data object NewsStar : Destinations("news_star")
    data object NewsDetail : Destinations("news_detail")
    data object ApplicationEdit : Destinations("application_edit")
    data object Message : Destinations("message")
    data object Setting : Destinations("setting")
    data object About : Destinations("about")
    data object ClassroomSearch : Destinations("classroom_search")
    data object WebView : Destinations("webview")
    data object License : Destinations("license")
    data object LibrarySearch : Destinations("library_search")
    data object AirCondition : Destinations("air_condition")
    data object AirConditionSetting : Destinations("air_condition_setting")
    data object AccountManage : Destinations("account_manage")
    data object Grade : Destinations("grade")
    data object TeacherEvaluation : Destinations("teacher_evaluation")
    data object Textbook : Destinations("textbook")
    data object TextbookSelect : Destinations("textbook_select")
    data object CourseTable : Destinations("course_table")
    data object Feedback : Destinations("feedback")
    data object WebsiteNavigation : Destinations("website_navigation")
    data object AIConfiguration : Destinations("ai_configuration")
}