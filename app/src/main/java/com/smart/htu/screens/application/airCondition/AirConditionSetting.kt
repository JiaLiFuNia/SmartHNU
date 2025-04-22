package com.smart.htu.screens.application.airCondition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.textButtonPrimaryColors
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirConditionSetting(
    navController: NavController,
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackBarHostState = viewModel.snackBarHostState
    var buildingId by remember { mutableStateOf(uiState.buildingCode) }
    var roomId by remember { mutableStateOf(uiState.roomCode) }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
        top.yukonga.miuix.kmp.basic.LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = it.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
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
                    label = "宿舍楼",
                    onValueChange = {
                        buildingId = it
                        onBuildingError(!buildingIdPattern.matches(it))
                    },
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
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = roomId,
                    label = "房间",
                    onValueChange = {
                        roomId = it
                        onRoomError(!roomIdPattern.matches(it) || roomId.length != 4)
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    trailingIcon = {
                        if (roomIdError)
                            top.yukonga.miuix.kmp.basic.Icon(
                                painter = painterResource(id = R.drawable.warning_24px),
                                contentDescription = "warning",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                    }
                )
            }
            item {
                SmallTitle(
                    text = "Cookie",
                    insideMargin = PaddingValues(12.dp, 8.dp)
                )
                val dropdownOptions = listOf("云端", "自定义")
                Card {
                    SuperDropdown(
                        title = "Cookie 来源",
                        summary = "云端 Cookie 由开发者提供，自定义 Cookie 需用户自行抓包获取",
                        items = dropdownOptions,
                        selectedIndex = uiState.setCookieType,
                        mode = DropDownMode.AlwaysOnRight,
                        onSelectedIndexChange = {
                            viewModel.changeCookieType(it)
                        }
                    )
                }
            }
            item {
                AnimatedVisibility(visible = uiState.setCookieType == 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            label = "shiroJID",
                            value = uiState.userLoginCookie?.shiroJID ?: "",
                            onValueChange = { viewModel.changeUserCookieSY(shiroJID = it) }
                        )
                        TextField(
                            label = "ymId",
                            value = uiState.userLoginCookie?.ymId ?: "",
                            onValueChange = { viewModel.changeUserCookieSY(ymId = it) }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(8.dp))
                top.yukonga.miuix.kmp.basic.TextButton(
                    text = "测试",
                    onClick = {
                        scope.launch {
                            viewModel.getAirConditionConfig()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            item {
                top.yukonga.miuix.kmp.basic.TextButton(
                    text = "保存",
                    enabled = uiState.isCookieValid && buildingId.isNotEmpty() && roomId.isNotEmpty(),
                    onClick = {
                        viewModel.saveBuildingAndRoomId(buildingId, roomId)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.textButtonPrimaryColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}