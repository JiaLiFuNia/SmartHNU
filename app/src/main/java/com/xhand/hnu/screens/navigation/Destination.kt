package com.xhand.hnu.screens.navigation

sealed class Destinations(val route: String) {
    data object Person : Destinations("Person")
    data object Grade : Destinations("Grade")
    data object Message : Destinations("Message")
    data object ClassroomSearch : Destinations("ClassroomSearch")
    data object CourseSearch : Destinations("CourseSearch")
    data object BookSelect : Destinations("BookSelect")
    data object ClassTask : Destinations("ClassTask")
    data object Teacher : Destinations("Teacher")
    data object SecondClass : Destinations("SecondClass")
    data object News : Destinations("News")
    data object NewsDetail : Destinations("NewsDetail")
    data object Setting : Destinations("Setting")
    data object Guide : Destinations("Guide")
}