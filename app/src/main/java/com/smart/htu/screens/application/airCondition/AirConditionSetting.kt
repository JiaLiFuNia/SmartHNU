package com.smart.htu.screens.application.airCondition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.BottomCircularProgressIndicator
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TextFieldDefaults
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun AirConditionSetting(
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()

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

    val backdrop = rememberBlurBackdrop(uiState.blurEffect)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    title = "配置",
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
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
                                    !uiState.isCheckingConfig
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Ok,
                                contentDescription = "test"
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical()
                    .scrollEndHaptic(),
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
                        OverlayDropdownPreference(
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
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                            labelColor = if (roomIdError) MaterialTheme.colorScheme.error else MiuixTheme.colorScheme.onSecondaryContainer,
                        ),
                        singleLine = true,
                        maxLines = 1,
                        trailingIcon = {
                            if (buildingIdError)
                                Icon(
                                    imageVector = Icons.Outlined.Warning,
                                    contentDescription = "warning",
                                    tint = MiuixTheme.colorScheme.error,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                        }
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
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                            labelColor = if (roomIdError) MaterialTheme.colorScheme.error else MiuixTheme.colorScheme.onSecondaryContainer,
                        ),
                        trailingIcon = {
                            if (roomIdError)
                                Icon(
                                    imageVector = Icons.Outlined.Warning,
                                    contentDescription = "warning",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                        }
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
                            OverlayDropdownPreference(
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
        }
        val isCheckingConfig by remember(uiState.isCheckingConfig) { mutableStateOf(uiState.isCheckingConfig) }
        BottomCircularProgressIndicator(
            loadingState = isCheckingConfig,
        )
    }
}