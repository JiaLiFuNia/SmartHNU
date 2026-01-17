package com.smart.htu.screens.application.airCondition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.BottomCircularProgressIndicator
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AirConditionSetting(
    navController: NavController,
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val hazeState = rememberHazeState()

    val shiroJID = remember { mutableStateOf(uiState.userLoginCookie?.shiroJID ?: "") }
    val ymId = remember { mutableStateOf(uiState.userLoginCookie?.ymId ?: "") }
    val buildingId = remember { mutableStateOf(uiState.buildingId) }
    val roomId = remember { mutableStateOf(uiState.roomId) }
    val selectedCampusIndex = remember { mutableIntStateOf(uiState.campusId) }

    val buildingIdPattern = Regex("^\\d{2}$")
    val roomIdPattern = Regex("^\\d{4}$")
    val (buildingIdError, onBuildingError) = remember { mutableStateOf(false) }
    val (roomIdError, onRoomError) = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val scrollBehavior = MiuixScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = "配置",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Close,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                focusManager.clearFocus()
                                viewModel.saveACConfig(
                                    campusLocation = selectedCampusIndex.intValue,
                                    buildingId = buildingId.value,
                                    roomId = roomId.value,
                                    shiroJID = shiroJID.value,
                                    ymId = ymId.value
                                )
                            }
                        },
                        enabled = buildingId.value.isNotEmpty() &&
                                roomId.value.isNotEmpty() &&
                                !buildingIdError &&
                                !roomIdError &&
                                !uiState.isCheckingConfig,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Ok,
                            contentDescription = "test"
                        )
                    }
                },
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = true
                    },
            )
        },
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding() + 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical()
                .hazeSource(hazeState),
            overscrollEffect = null
        ) {
            item {
                SuggestChip(
                    text = "当前仅建设东路校区可用",
                    onClick = {},
                    type = SuggestChipType.INFO,
                    icon = Icons.Outlined.Info
                )
            }
            item {
                SmallTitle(
                    text = "宿舍楼和房间",
                    insideMargin = PaddingValues(12.dp, 8.dp)
                )
                Card {
                    SuperDropdown(
                        title = "选择校区",
                        items = listOf("建设东路西校区", "建设东路东校区"),
                        selectedIndex = selectedCampusIndex.intValue,
                        onSelectedIndexChange = {
                            selectedCampusIndex.intValue = it
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = buildingId.value,
                    label = "宿舍楼号",
                    onValueChange = {
                        buildingId.value = it
                        onBuildingError(!buildingIdPattern.matches(it))
                    },
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        if (buildingIdError)
                            Icon(
                                painter = painterResource(id = R.drawable.warning_24px),
                                contentDescription = "warning",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                    },
                    labelColor = if (buildingIdError) MaterialTheme.colorScheme.error else MiuixTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = roomId.value,
                    label = "房间号",
                    onValueChange = {
                        roomId.value = it
                        onRoomError(!roomIdPattern.matches(it) || roomId.value.length != 4)
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                    trailingIcon = {
                        if (roomIdError)
                            Icon(
                                painter = painterResource(id = R.drawable.warning_24px),
                                contentDescription = "warning",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                    },
                    labelColor = if (roomIdError) MaterialTheme.colorScheme.error else MiuixTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "示例：1号楼101房间，填写为：宿舍楼号：01，房间号：0101",
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            item {
                SmallTitle(
                    text = "Cookie",
                    insideMargin = PaddingValues(12.dp, 8.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card {
                        SuperDropdown(
                            title = "Cookie 来源",
                            summary = "云端 Cookie 由开发者提供，自定义 Cookie 需用户自行抓包获取",
                            items = listOf("云端", "自定义"),
                            selectedIndex = uiState.cookieType,
                            onSelectedIndexChange = {
                                viewModel.changeLoginCookieType(it)
                            }
                        )
                    }
                    AnimatedVisibility(
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically(),
                        visible = uiState.cookieType == 1
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextField(
                                label = "shiroJID",
                                value = shiroJID.value,
                                onValueChange = { shiroJID.value = it }
                            )
                            TextField(
                                label = "ymId",
                                value = ymId.value,
                                onValueChange = { ymId.value = it }
                            )
                        }
                    }
                }
            }
        }
        BottomCircularProgressIndicator(
            loadingState = remember(uiState.isCheckingConfig) { mutableStateOf(uiState.isCheckingConfig) }
        )
    }
}