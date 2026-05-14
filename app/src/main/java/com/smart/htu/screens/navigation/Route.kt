package com.smart.htu.screens.navigation

// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

import androidx.navigation3.runtime.NavKey
import com.smart.htu.api.module.CourseSearchPostEntity
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation keys for Navigation3.
 * Each destination is a NavKey (data object/data class) and can be saved/restored in the back stack.
 */

sealed interface Route : NavKey {
    @Serializable
    data object Main : Route

    @Serializable
    data object Application : Route

    @Serializable
    data object News : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Person : Route

    @Serializable
    data object NewsSearch : Route

    @Serializable
    data object NewsMark : Route

    @Serializable
    data class NewsDetail(val url: String, val title: String, val source: String) : Route

    @Serializable
    data object Message : Route

    @Serializable
    data object About : Route

    @Serializable
    data object ClassroomSearch : Route

    @Serializable
    data class ApplicationWebView(val url: String, val title: String) : Route

    @Serializable
    data object License : Route

    @Serializable
    data object LibrarySearch : Route

    @Serializable
    data class LibrarySearchDetail(val bookId: String) : Route

    @Serializable
    data object AirCondition : Route

    @Serializable
    data object AirConditionSetting : Route

    @Serializable
    data object AccountManage : Route

    @Serializable
    data object Grade : Route

    @Serializable
    data object TeacherEvaluation : Route

    @Serializable
    data class TeacherEvaluationDetail(
        val syllabusEvaluateCode: String,
        val teacherCode: String
    ) : Route

    @Serializable
    data object Textbook : Route

    @Serializable
    data class TextbookSelect(val courseTaskCode: String, val termCode: String) : Route

    @Serializable
    data object CourseTable : Route

    @Serializable
    data object Feedback : Route

    @Serializable
    data object WebsiteNavigation : Route

    @Serializable
    data object AIConfiguration : Route

    @Serializable
    data object HomeContentSettings : Route

    @Serializable
    data class PdfReaderView(val url: String, val title: String) : Route

    @Serializable
    data object MessageBoard : Route

    @Serializable
    data class MessageBoardDetail(val postID: String) : Route

    @Serializable
    data object CampusLife : Route

    @Serializable
    data object ArticleStyle : Route

    @Serializable
    data object TaskManager : Route

    @Serializable
    data object SecondClass : Route

    @Serializable
    data object CourseHelper : Route

    @Serializable
    data class CourseRepo(
        val courseTypeName: String,
        val courseTypeId: String
    ) : Route

    @Serializable
    data class CourseInfo(val courseTaskCode: String) : Route

    @Serializable
    data object CourseSearch : Route

    @Serializable
    data class CourseSearchRepo(val searchInfo: CourseSearchPostEntity) : Route

    @Serializable
    data object EmojiEasterEgg : Route

    @Serializable
    data object CampusCard : Route

    @Serializable
    data object ConsumptionRecord : Route
}
