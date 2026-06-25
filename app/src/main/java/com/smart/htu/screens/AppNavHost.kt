package com.smart.htu.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.smart.htu.component.PDFViewer
import com.smart.htu.screens.application.airCondition.AirCondition
import com.smart.htu.screens.application.airCondition.AirConditionHistory
import com.smart.htu.screens.application.airCondition.AirConditionSetting
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.campusCard.CampusCardScreen
import com.smart.htu.screens.application.campusCard.ConsumptionRecordScreen
import com.smart.htu.screens.application.campusLife.CampusLife
import com.smart.htu.screens.application.classroom.ClassroomSearchScreen
import com.smart.htu.screens.application.courseHelper.CourseHelper
import com.smart.htu.screens.application.courseHelper.CourseHelperViewModel
import com.smart.htu.screens.application.courseHelper.CourseInfo
import com.smart.htu.screens.application.courseHelper.CourseRepo
import com.smart.htu.screens.application.courseSearch.CourseSearch
import com.smart.htu.screens.application.courseSearch.CourseSearchRepo
import com.smart.htu.screens.application.courseTable.CourseTable
import com.smart.htu.screens.application.grade.Grade
import com.smart.htu.screens.application.librarySearch.LibrarySearchDetail
import com.smart.htu.screens.application.librarySearch.LibrarySearchScreen
import com.smart.htu.screens.application.messageBoard.MessageBoard
import com.smart.htu.screens.application.messageBoard.MessageBoardDetail
import com.smart.htu.screens.application.physicalTest.AddPhysicalTestScore
import com.smart.htu.screens.application.physicalTest.PhysicalTest
import com.smart.htu.screens.application.secondClass.SecondClass
import com.smart.htu.screens.application.taskManager.TaskManager
import com.smart.htu.screens.application.teacherEvaluation.TeacherEvaluation
import com.smart.htu.screens.application.teacherEvaluation.TeacherEvaluationDetail
import com.smart.htu.screens.application.textbook.Textbook
import com.smart.htu.screens.application.textbook.TextbookSelect
import com.smart.htu.screens.application.websiteNavigation.WebsiteNavigation
import com.smart.htu.screens.application.webview.ApplicationWebView
import com.smart.htu.screens.login.LoginScreen
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.message.MessageScreen
import com.smart.htu.screens.message.MessageViewModel
import com.smart.htu.screens.navigation.Navigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.screens.news.NewsMark
import com.smart.htu.screens.news.NewsSearch
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.news.newsView.NewsDetail
import com.smart.htu.screens.person.AccountManage
import com.smart.htu.screens.person.PersonScreen
import com.smart.htu.screens.setting.AIConfigurationScreen
import com.smart.htu.screens.setting.About
import com.smart.htu.screens.setting.ArticleStyle
import com.smart.htu.screens.setting.EmojiEasterEgg
import com.smart.htu.screens.setting.HomeContentSettings
import com.smart.htu.screens.setting.License
import com.smart.htu.screens.setting.SettingViewModel
import com.smart.htu.screens.setting.ThemeSetting
import com.smart.htu.screens.setting.feedback.Feedback
import com.smart.htu.ui.theme.SmartHNUTheme
import com.smart.htu.ui.theme.keyColorFor
import top.yukonga.miuix.kmp.basic.Surface

val LocalNavigator = staticCompositionLocalOf<Navigator> { error("No navigator found!") }

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost() {
    val mainViewModel: MainViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val newsViewModel: NewsViewModel = hiltViewModel()
    val airConditionViewModel: AirConditionViewModel = hiltViewModel()
    val settingViewModel: SettingViewModel = hiltViewModel()
    val messageViewModel: MessageViewModel = hiltViewModel()
    val courseHelperViewModel: CourseHelperViewModel = hiltViewModel()

    val uiState = settingViewModel.uiState.collectAsState().value
    val backStack = remember { mutableStateListOf<NavKey>().apply { add(Route.Main) } }
    val navigator = remember(backStack) { Navigator(backStack) }

    CompositionLocalProvider(
        LocalNavigator provides navigator
    ) {
        SmartHNUTheme(
            colorMode = uiState.themeMode,
            keyColor = keyColorFor(uiState.keyColorSeedIndex)
        ) {
            Surface {
                NavDisplay(
                    backStack = navigator.backStack,
                    entryProvider = entryProvider {
                        entry<Route.Main> {
                            MainFrame(
                                mainViewModel = mainViewModel,
                                loginViewModel = loginViewModel,
                                messageViewModel = messageViewModel,
                                airConditionViewModel = airConditionViewModel,
                                settingViewModel = settingViewModel
                            )
                        }
                        entry<Route.Login> {
                            LoginScreen(viewModel = loginViewModel)
                        }
                        entry<Route.Person> {
                            PersonScreen(loginViewModel)
                        }
                        entry<Route.Message> {
                            MessageScreen(viewModel = messageViewModel)
                        }
                        entry<Route.ApplicationWebView> {
                            ApplicationWebView(
                                url = it.url,
                                title = it.title,
                            )
                        }
                        entry<Route.ClassroomSearch> {
                            ClassroomSearchScreen()
                        }
                        entry<Route.LibrarySearch> {
                            LibrarySearchScreen()
                        }
                        entry<Route.LibrarySearchDetail> {
                            LibrarySearchDetail(bookId = it.bookId)
                        }
                        entry<Route.AirCondition> {
                            AirCondition(viewModel = airConditionViewModel)
                        }
                        entry<Route.AirConditionSetting> {
                            AirConditionSetting(viewModel = airConditionViewModel)
                        }
                        entry<Route.AirConditionHistory> {
                            AirConditionHistory(viewModel = airConditionViewModel)
                        }
                        entry<Route.AccountManage> {
                            AccountManage(viewModel = loginViewModel)
                        }
                        entry<Route.About> {
                            About()
                        }
                        entry<Route.License> {
                            License()
                        }
                        entry<Route.Grade> {
                            Grade()
                        }
                        entry<Route.CourseTable> {
                            CourseTable()
                        }
                        entry<Route.TeacherEvaluation> {
                            TeacherEvaluation()
                        }
                        entry<Route.TeacherEvaluationDetail> {
                            TeacherEvaluationDetail(
                                syllabusEvaluateCode = it.syllabusEvaluateCode,
                                teacherCode = it.teacherCode
                            )
                        }
                        entry<Route.Textbook> {
                            Textbook()
                        }
                        entry<Route.TextbookSelect> {
                            TextbookSelect(
                                courseTaskCode = it.courseTaskCode,
                                termCode = it.termCode
                            )
                        }
                        entry<Route.NewsSearch> {
                            NewsSearch(viewModel = newsViewModel)
                        }
                        entry<Route.NewsMark> {
                            NewsMark()
                        }
                        entry<Route.NewsDetail> {
                            NewsDetail(
                                url = it.url,
                                title = it.title,
                                source = it.source
                            )
                        }
                        entry<Route.Feedback> {
                            Feedback()
                        }
                        entry<Route.WebsiteNavigation> {
                            WebsiteNavigation()
                        }
                        entry<Route.AIConfiguration> {
                            AIConfigurationScreen(settingViewModel)
                        }
                        entry<Route.HomeContentSettings> {
                            HomeContentSettings(settingViewModel)
                        }
                        entry<Route.MessageBoard> {
                            MessageBoard()
                        }
                        entry<Route.MessageBoardDetail> {
                            MessageBoardDetail(postID = it.postID)
                        }
                        entry<Route.CampusLife> {
                            CampusLife()
                        }
                        entry<Route.ArticleStyle> {
                            ArticleStyle(settingViewModel)
                        }
                        entry<Route.TaskManager> {
                            TaskManager()
                        }
                        entry<Route.SecondClass> {
                            SecondClass()
                        }
                        entry<Route.CourseHelper> {
                            CourseHelper()
                        }
                        entry<Route.CourseInfo> {
                            CourseInfo(
                                courseCode = it.courseTaskCode,
                                termCode = it.termCode
                            )
                        }
                        entry<Route.CourseRepo> {
                            CourseRepo(
                                courseTypeId = it.courseTypeId,
                                courseTypeName = it.courseTypeName
                            )
                        }
                        entry<Route.CourseSearch> {
                            CourseSearch()
                        }
                        entry<Route.CourseSearchRepo> {
                            CourseSearchRepo(searchInfo = it.searchInfo)
                        }
                        entry<Route.PdfReaderView> {
                            PDFViewer(
                                url = it.url,
                                title = it.title
                            )
                        }
                        entry<Route.EmojiEasterEgg> {
                            EmojiEasterEgg()
                        }
                        entry<Route.CampusCard> {
                            CampusCardScreen()
                        }
                        entry<Route.ConsumptionRecord> {
                            ConsumptionRecordScreen()
                        }
                        entry<Route.ThemeSetting> {
                            ThemeSetting(settingViewModel)
                        }
                        entry<Route.PhysicalTest> {
                            PhysicalTest()
                        }
                        entry<Route.AddPhysicalTestScore> {
                            AddPhysicalTestScore(
                                grade = it.grade
                            )
                        }
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    onBack = { navigator.pop() },
                )
            }
        }
    }

}