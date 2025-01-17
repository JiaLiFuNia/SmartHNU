package com.smart.htu.screens.application.airCondition

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.PreferenceSubtitle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AirCondition(
    navController: NavController,
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            viewModel.getBillDetailService()
            delay(1000)
            isRefreshing = false
        }
    }
    LaunchedEffect(Unit) {
        onRefresh()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surfaceContainer
                ),
                title = { Text(text = "寝室电费") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                bottomState.expand()
                                openBottomSheet = true
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular()
                ) {
                    blurRadius = 30.dp
                    blurEnabled = uiState.blurEffect
                }
            )
        }
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = it.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = state
                )
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 15.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 15.dp
                ),
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
            ) {
                item {
                    TextButton(onClick = { viewModel.getBillDetailService() }) {
                        Text(text = "测试")
                    }
                }
                item {
                    Text(text = uiState.billData.toString(), modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
    if (openBottomSheet)
        SetCookieBottomSheet(
            bottomState = bottomState,
            uiState = uiState,
            viewModel = viewModel,
            onDismissRequest = { openBottomSheet = false }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetCookieBottomSheet(
    uiState: AirConditionUiState,
    viewModel: AirConditionViewModel,
    bottomState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        sheetState = bottomState,
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Text(
            text = "设置信息",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(15.dp)
        ) {
            item {
                PreferenceSubtitle(text = "宿舍楼和房间")
                var buildingId by remember { mutableStateOf(uiState.buildingCode) }
                var roomId by remember { mutableStateOf(uiState.roomCode) }
                val buildingIdPattern = Regex("^[西|东]\\d{2}$")
                val roomIdPattern = Regex("^\\d{4}$")
                var buildingIdError by remember { mutableStateOf(false) }
                var roomIdError by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        label = { Text(text = "宿舍楼") },
                        placeholder = { Text(text = "如：西01 或 东02") },
                        maxLines = 1,
                        value = buildingId,
                        onValueChange = {
                            buildingId = it
                            buildingIdError = !buildingIdPattern.matches(it)
                        },
                        trailingIcon = {
                            if (buildingIdError) Icon(
                                painter = painterResource(id = R.drawable.warning_24px),
                                contentDescription = "warning",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        isError = buildingIdError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        label = { Text(text = "房间") },
                        placeholder = { Text(text = "如：0123") },
                        value = roomId,
                        onValueChange = {
                            roomId = it
                            roomIdError = !roomIdPattern.matches(it) || roomId.length != 4
                        },
                        trailingIcon = {
                            if (roomIdError) Icon(
                                painter = painterResource(id = R.drawable.warning_24px),
                                contentDescription = "warning",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        isError = roomIdError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextButton(
                        onClick = {
                            viewModel.saveBuildingAndRoomId(buildingId, roomId)
                            viewModel.getBillDetailService()
                        },
                        enabled = !buildingIdError && !roomIdError
                    ) {
                        Text(text = "确定")
                    }
                }
            }
            item {
                PreferenceSubtitle(text = "设置Cookie")
                val radioOptions = listOf("开发者Cookie", "自定义Cookie")
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(Modifier.selectableGroup()) {
                        radioOptions.forEachIndexed { index, text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .selectable(
                                        selected = (index == uiState.setCookieType),
                                        onClick = { viewModel.changeCookieType(index) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (index == uiState.setCookieType),
                                    onClick = null
                                )
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(visible = uiState.setCookieType == 1) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            label = { Text(text = "shiroJID") },
                            placeholder = { },
                            maxLines = 1,
                            value = uiState.remoteLoginCookie?.shiroJID ?: "",
                            onValueChange = { viewModel.changeShiroJid(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            label = { Text(text = "ymID") },
                            placeholder = { },
                            value = uiState.remoteLoginCookie?.ymId ?: "",
                            onValueChange = { viewModel.changeYmld(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextButton(onClick = { viewModel.getAirConditionConfig() }) {
                            Text(text = "测试")
                        }
                    }
                }
            }
        }
    }
}