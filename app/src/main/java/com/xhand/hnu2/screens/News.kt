package com.xhand.hnu2.screens

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xhand.hnu2.R
import com.xhand.hnu2.components.ArticleListItem
import com.xhand.hnu2.components.ModalBottomSheet
import com.xhand.hnu2.viewmodel.LocalNewsViewModel
import com.xhand.hnu2.viewmodel.NewsViewModel
import com.xhand.hnu2.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch


@Composable
fun NavigationScreen(viewModel: SettingsViewModel) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNewsViewModel provides NewsViewModel()) {
        val newsViewModel = LocalNewsViewModel.current
        NavHost(
            navController = navController,
            startDestination = "newsList_screen",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            composable("newsList_screen") {
                NewsScreen(
                    navController = navController,
                    newsViewModel = newsViewModel,
                    viewModel = viewModel
                )
            }
            composable("detail_screen") {
                ArticleDetailScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NewsScreen(
    navController: NavController,
    newsViewModel: NewsViewModel,
    viewModel: SettingsViewModel
) {
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val options = listOf(
        "通知公告", "师大要闻", "新闻速递", "教务通知", "公示公告", "考务管理"
    )
    var selectedOption by remember { mutableStateOf(options[0]) }
    var isShowSearchBar by remember {
        mutableStateOf(false)
    }
    var isSearch by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = newsViewModel.isRefreshing,
        onRefresh = {
            scope.launch {
                newsViewModel.newsList()
            }
        }
    )
    LaunchedEffect(Unit) {
        newsViewModel.newsList()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(colorScheme.surface.copy())

                ),
                title = { Text("新闻") },
                actions = {
                    if (!isShowSearchBar) {
                        IconButton(
                            onClick = {
                                isShowSearchBar = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "搜索"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                isShowSearchBar = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                        val interactionSource = remember { MutableInteractionSource() }
                        BasicTextField(
                            value = newsViewModel.content,
                            onValueChange = { newsViewModel.content = it },
                            interactionSource = interactionSource,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = colorScheme.onSurface
                            )
                        ) {
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = newsViewModel.content,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                interactionSource = interactionSource,
                                visualTransformation = VisualTransformation.None,
                                leadingIcon = {
                                    IconButton(onClick = { isSearch = true }) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "搜索"
                                        )
                                    }
                                },
                                placeholder = { Text(text = "请输入关键词...") },
                                container = {
                                    OutlinedTextFieldDefaults.ContainerBox(
                                        enabled = true,
                                        isError = false,
                                        interactionSource = interactionSource,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                        ),
                                        focusedBorderThickness = 0.dp,
                                        unfocusedBorderThickness = 0.dp,
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet.value = true
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_filter),
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        if (isSearch or newsViewModel.isSearching) {
            LaunchedEffect(Unit) {
                newsViewModel.searchRes()
            }
        }
        Box(
            modifier = Modifier.pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues = it)
            ) {
                if (newsViewModel.newsIsLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(600.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    if (!isShowSearchBar) {
                        items(newsViewModel.list) { article ->
                            if (article.type == selectedOption) {
                                ArticleListItem(
                                    article = article,
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate("detail_screen")
                                            viewModel.url = article.url
                                        }
                                )
                            }
                        }
                    } else {
                        if (!newsViewModel.isSearched) {
                            items(newsViewModel.searchList) { article ->
                                ArticleListItem(
                                    article = article,
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate("detail_screen")
                                            viewModel.url = article.url
                                        }
                                )
                            }
                        } else {
                            if (newsViewModel.content.isNotEmpty() and newsViewModel.isSearched and isSearch) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) { Text(text = "未检索到相关新闻") }
                                }
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = newsViewModel.isRefreshing,
                state = pullRefreshState
            )
        }


    }
    ModalBottomSheet(showModalBottomSheet = showBottomSheet, text = "选择新闻源") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option }
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = null
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun NewsScreenPreview() {
    NavigationScreen(viewModel = SettingsViewModel())
}