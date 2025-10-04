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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.TextButtonWithProgressIndicator
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirConditionSetting(
    navController: NavController,
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var shiroJID by remember { mutableStateOf(uiState.userLoginCookie?.shiroJID ?: "") }
    var ymId by remember { mutableStateOf(uiState.userLoginCookie?.ymId ?: "") }
    var buildingId by remember { mutableStateOf(uiState.buildingCode) }
    var roomId by remember { mutableStateOf(uiState.roomCode) }

    val snackBarHostState = viewModel.snackBarHostState
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = {
                    Text("配置")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding() + 8.dp,
                bottom = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null
        ) {
            item {
                SmallTitle(
                    text = "宿舍楼和房间",
                    insideMargin = PaddingValues(12.dp, 8.dp)
                )
                val buildingIdPattern = Regex("^[西|东]\\d{2}$")
                val roomIdPattern = Regex("^\\d{4}$")
                val (buildingIdError, onBuildingError) = remember { mutableStateOf(false) }
                val (roomIdError, onRoomError) = remember { mutableStateOf(false) }
                TextField(
                    value = buildingId,
                    label = "宿舍楼(如：西01)",
                    onValueChange = {
                        buildingId = it
                        onBuildingError(!buildingIdPattern.matches(it))
                    },
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down)
                    },
                    useLabelAsPlaceholder = true,
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        if (buildingIdError)
                            top.yukonga.miuix.kmp.basic.Icon(
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
                    value = roomId,
                    label = "房间(如：0123，01楼23房间)",
                    onValueChange = {
                        roomId = it
                        onRoomError(!roomIdPattern.matches(it) || roomId.length != 4)
                    },
                    useLabelAsPlaceholder = true,
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    trailingIcon = {
                        if (roomIdError)
                            top.yukonga.miuix.kmp.basic.Icon(
                                painter = painterResource(id = R.drawable.warning_24px),
                                contentDescription = "warning",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                    },
                    labelColor = if (roomIdError) MaterialTheme.colorScheme.error else MiuixTheme.colorScheme.onSecondaryContainer,
                )
            }
            item {
                SmallTitle(
                    text = "Cookie",
                    insideMargin = PaddingValues(12.dp, 8.dp)
                )
                val dropdownOptions = listOf("云端", "自定义")
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
                            items = dropdownOptions,
                            selectedIndex = uiState.cookieType,
                            mode = DropDownMode.AlwaysOnRight,
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
                                value = shiroJID,
                                onValueChange = { shiroJID = it }
                            )
                            TextField(
                                label = "ymId",
                                value = ymId,
                                onValueChange = { ymId = it }
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                TextButtonWithProgressIndicator(
                    text = "保存",
                    enabled = buildingId.isNotEmpty() && roomId.isNotEmpty(),
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.saveACConfig(
                            buildingId = buildingId,
                            roomId = roomId,
                            shiroJID = shiroJID,
                            ymId = ymId,
                            onFailure = {
                                viewModel.showSnackBar("Cookie 无效，请重新填写")
                            },
                            onSuccess = {
                                navController.popBackStack()
                                viewModel.showSnackBar("配置成功！下拉刷新获取数据")
                            }
                        )
                    },
                    isLoading = uiState.isCheckingConfig,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}