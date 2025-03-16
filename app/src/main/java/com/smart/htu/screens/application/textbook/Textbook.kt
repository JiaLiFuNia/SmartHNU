package com.smart.htu.screens.application.textbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.CourseTextbook
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.InfoBadge
import com.smart.htu.component.ScaffoldWithHazeLazyColumn
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.screens.application.grade.SelectTermBottomSheet
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Term.termConverter
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Textbook(
    themeMode: Int,
    viewModel: TextbookViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val state = rememberPullToRefreshState()

    val (isBottomSheetShow, onShowBottomSheet) = remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.getTextbook(uiState.termCode)
            isRefreshing = false
        }
    }

    ScaffoldWithHazeLazyColumn(
        themeMode = themeMode,
        isMediumTopAppBar = true,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        blurEnabledState = uiState.blurEffect,
        title = { Text(text = stringResource(id = R.string.textbook_select)) },
        actions = {
            IconButton(onClick = { onShowBottomSheet(true) }) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "more")
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        refreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState(),
        onRefresh = { onRefresh() },
        itemSpacePadding = 12.dp
    ) {
        when (uiState.courseList.status == Status.LOADING || !uiState.isTokenValid) {
            true ->
                item {
                    CircularProgressIndicator()
                }

            false -> {
                if (uiState.courseList.data?.courseTextbookList?.isEmpty() == true) {
                    item {
                        EmptyContent(
                            text = "学期 ${termConverter(uiState.termCode)}\n暂无数据",
                            image = DrawableVectors.emptyData()
                        )
                    }
                } else {
                    items(uiState.courseList.data?.courseTextbookList ?: emptyList()) {
                        SingleCourseTextbook(uiState.termCode, it, navController, themeMode)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    SelectTermBottomSheet(
        termSelectedCode = uiState.termCode,
        termList = uiState.termIndex,
        isBottomSheetShow = isBottomSheetShow,
        onDismissRequest = {
            onShowBottomSheet(false)
        },
        onClick = {
            viewModel.changeTermCode(it)
            onRefresh()
        }
    )
}

@Composable
fun SingleCourseTextbook(
    termCode: String,
    course: CourseTextbook,
    navController: NavController,
    themeMode: Int
) {
    Surface(
        onClick = {
            navController.navigate("${Destinations.TextbookSelect.route}/${course.courseTaskCode}/${termCode}")
        },
        modifier = Modifier
            .semantics { role = Role.Button }
            .fillMaxWidth(),
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        color = if (themeMode == 0) MiuixTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoBadge(course.courseCategory)
                    InfoBadge(course.courseClassification)
                    InfoBadge(course.description)
                }
            },
            trailingContent = {
                if (!course.isNeedTextbook)
                    Text(
                        text = course.type,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                else
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "back"
                    )
            }
        )
    }
}