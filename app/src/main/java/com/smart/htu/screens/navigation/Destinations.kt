package com.smart.htu.screens.navigation

sealed class Destinations(
    val route: String
) {
    data object App : Destinations("main")
    data object Login : Destinations("login")
    data object News : Destinations("news")
    data object Person : Destinations("person")
    data object Application : Destinations("application")
    data object Message : Destinations("message")
    data object Setting : Destinations("setting")
    data object About : Destinations("about")
    data object DynamicColorSetting : Destinations("dynamic")
    data object MainSetting : Destinations("main_setting")
    data object NewsSetting : Destinations("news_setting")
    data object AppSetting : Destinations("app_setting")
    data object ClassroomSearch : Destinations("classroom_search")
    data object WebView : Destinations("webview")
    data object Appreciate : Destinations("appreciate")
    data object LibrarySearch : Destinations("library_search")
    data object AirCondition : Destinations("air_condition")
    data object AccountManage : Destinations("account_manage")
}