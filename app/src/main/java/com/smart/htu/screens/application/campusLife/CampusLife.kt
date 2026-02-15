package com.smart.htu.screens.application.campusLife

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.Constants.Companion.CAINIAO_URL
import com.smart.htu.utils.Constants.Companion.PINDUODUO_URL
import com.smart.htu.utils.Constants.Companion.TAOBAO_URL
import com.smart.htu.utils.startActivityWithUri
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CampusLife(
    viewModel: CampusLifeViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    // val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val hazeState = rememberHazeState()

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(R.string.campus_life),
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                ) {
                    blurRadius = 30.dp
                    noiseFactor = 0f
                    blurEnabled = true
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .hazeSource(hazeState)
                .overScrollVertical(),
            overscrollEffect = null,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = it.calculateTopPadding(),
                end = 16.dp,
                bottom = it.calculateBottomPadding() + 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SmallTitle(
                    text = "快递服务",
                    insideMargin = PaddingValues(12.dp, 8.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((56 * 2 + 12).dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    item {
                        Card(
                            modifier = Modifier,
                            colors = CardDefaults.defaultColors(MiuixTheme.colorScheme.surfaceContainer)
                        ) {
                            BasicComponent(
                                title = "拼多多取件码",
                                onClick = {
                                    startActivityWithUri(
                                        appName = "拼多多",
                                        uri = PINDUODUO_URL
                                    )
                                },
                                startAction = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_pinduoduo),
                                        contentDescription = "pinduoduo",
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .size(24.dp)
                                    )
                                }
                            )
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier,
                            colors = CardDefaults.defaultColors(MiuixTheme.colorScheme.surfaceContainer)
                        ) {
                            BasicComponent(
                                title = "淘宝取件码",
                                onClick = {
                                    startActivityWithUri(
                                        appName = "淘宝",
                                        packageName = "com.taobao.taobao",
                                        activityName = "com.taobao.browser.BrowserActivity",
                                        uri = TAOBAO_URL
                                    )
                                },
                                startAction = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_taobao),
                                        contentDescription = "pinduoduo",
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .size(24.dp)
                                    )
                                }
                            )
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier,
                            colors = CardDefaults.defaultColors(MiuixTheme.colorScheme.surfaceContainer)
                        ) {
                            BasicComponent(
                                title = "菜鸟取件码",
                                onClick = {
                                    startActivityWithUri(
                                        appName = "菜鸟",
                                        packageName = "com.cainiao.wireless",
                                        uri = CAINIAO_URL,
                                        extra = mapOf(
                                            "jumpPath" to "guoguo://go/station_code",
                                            "entrance" to "pinned_shortcuts_identity_code"
                                        )
                                    )
                                },
                                startAction = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_cainiao),
                                        contentDescription = "cainiao",
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .size(24.dp)
                                    )
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}