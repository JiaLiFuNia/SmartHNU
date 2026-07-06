package com.smart.htu.screens.application.campusLife

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.Constants.Companion.CAINIAO_URL
import com.smart.htu.utils.Constants.Companion.PINDUODUO_URL
import com.smart.htu.utils.Constants.Companion.TAOBAO_URL
import com.smart.htu.utils.startActivityWithUri
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun CampusLife(
    viewModel: CampusLifeViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    title = stringResource(R.string.campus_life),
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },

                            ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .scrollEndHaptic()
                    .overScrollVertical(),
                overscrollEffect = null,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = it.calculateTopPadding() + 16.dp,
                    end = 16.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SmallTitle(
                        text = "即时天气",
                        insideMargin = PaddingValues(12.dp, 8.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        when {
                            uiState.isWeatherLoading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    InfiniteProgressIndicator()
                                }
                            }

                            uiState.weatherError != null -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = uiState.weatherError ?: "",
                                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "点击重试",
                                        color = MiuixTheme.colorScheme.primary,
                                        fontSize = 14.sp,
                                        modifier = Modifier.clickable { viewModel.loadWeather() }
                                    )
                                }
                            }

                            uiState.weatherData != null -> {
                                val weather = uiState.weatherData!!
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${weather.temperature}°",
                                                fontSize = 56.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MiuixTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = weather.weather,
                                                fontSize = 16.sp,
                                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                            )
                                        }
                                        Image(
                                            painter = painterResource(weather.iconResId),
                                            contentDescription = weather.weather,
                                            modifier = Modifier.size(72.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    HorizontalDivider()

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        WeatherDetailItem(
                                            label = "体感",
                                            value = "${weather.feelsLike}°"
                                        )
                                        WeatherDetailItem(
                                            label = "湿度",
                                            value = "${weather.humidity}%"
                                        )
                                        WeatherDetailItem(
                                            label = "风向",
                                            value = weather.windDir
                                        )
                                        WeatherDetailItem(
                                            label = "风力",
                                            value = "${weather.windScale}级"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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
                                    onClick = {
                                        startActivityWithUri(
                                            appName = "拼多多",
                                            uri = PINDUODUO_URL
                                        )
                                    },
                                    startAction = {
                                        Image(
                                            painter = painterResource(R.drawable.pinduoduo),
                                            contentDescription = "pinduoduo",
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(24.dp)
                                        )
                                    }
                                ) {
                                    Text(
                                        text = "拼多多身份码",
                                        fontSize = MiuixTheme.textStyles.headline1.fontSize,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.basicMarquee(
                                            repeatDelayMillis = 2_000,
                                        )
                                    )
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier,
                                colors = CardDefaults.defaultColors(MiuixTheme.colorScheme.surfaceContainer)
                            ) {
                                BasicComponent(
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
                                            painter = painterResource(R.drawable.taobao),
                                            contentDescription = "pinduoduo",
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(24.dp)
                                        )
                                    }
                                ) {
                                    Text(
                                        text = "淘宝身份码",
                                        fontSize = MiuixTheme.textStyles.headline1.fontSize,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.basicMarquee(
                                            repeatDelayMillis = 2_000,
                                        )
                                    )
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier,
                                colors = CardDefaults.defaultColors(MiuixTheme.colorScheme.surfaceContainer)
                            ) {
                                BasicComponent(
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
                                            painter = painterResource(R.drawable.cainiao),
                                            contentDescription = "cainiao",
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(24.dp)
                                        )
                                    }
                                ) {
                                    Text(
                                        text = "菜鸟身份码",
                                        fontSize = MiuixTheme.textStyles.headline1.fontSize,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.basicMarquee(
                                            repeatDelayMillis = 2_000,
                                        )
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun WeatherDetailItem(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MiuixTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary
        )
    }
}

/*

@Composable
fun WeatherBottomSheet(
    isShowWeatherBottomSheet: Boolean,
    warningWeatherData: List<WarningWeatherData>,
    onDismissRequest: () -> Unit
) {
    OverlayBottomSheet(
        show = isShowWeatherBottomSheet,
        title = "天气预警",
        onDismissRequest = {
            onDismissRequest()
        },
        insideMargin = DpSize(16.dp, 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(warningWeatherData) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MiuixTheme.colorScheme.surface,
                ) {
                    BasicComponent(
                        title = it.title,
                        summary = it.content,
                        insideMargin = PaddingValues(horizontal = 0.dp)
                    )
                }
            }
        }
    }
}*/
