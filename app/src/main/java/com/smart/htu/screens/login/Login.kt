package com.smart.htu.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.LogTipDialog
import com.smart.htu.component.SettingItemCard
import com.smart.htu.component.WebView
import com.smart.htu.screens.person.PersonScreen

@SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
@Composable
fun LoginNavHostScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.loginState == 1)
        PersonScreen(
            navController = navController,
            logoutClick = { viewModel.changLoginState(0) },
            viewModel = viewModel
        )
    else
        LoginScreen(
            navController = navController,
            viewModel = viewModel
        )
    /*when (loginWay) {
        0 -> LoginScreen(
            navController = navController,
            viewModel = viewModel,
            changLoginWay = {
                loginWay = 1
            }
        )

        1 -> LoginWebView(
            navController = navController,
            viewModel = viewModel,
            changLoginWay = {
                loginWay = 0
            }
        )
    }*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var displayPassword by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    displayPassword = isPressed

    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login)) },
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
                    /*IconButton(onClick = { changLoginWay() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.swap_horiz_24px),
                            contentDescription = "null"
                        )
                    }*/
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "HNU 统一身份认证系统", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "account",
                                tint = if (uiState.loginState == -1 || uiState.loginState == 2 || uiState.studentID.length > 10)
                                    MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.primary
                            )
                        },
                        value = uiState.studentID,
                        onValueChange = {
                            viewModel.changeStudentID(it)
                        },
                        label = { Text(text = "学号") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.loginState == -1 || uiState.loginState == 2 || uiState.studentID.length > 10,
                        readOnly = uiState.isLoading,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.key_24px),
                                contentDescription = "key",
                                tint = if (uiState.loginState == -1 || uiState.loginState == 2)
                                    MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.primary
                            )
                        },
                        value = uiState.password,
                        onValueChange = {
                            viewModel.changePassword(it)
                        },
                        label = { Text(text = "密码") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        visualTransformation = if (displayPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { displayPassword = !displayPassword }) {
                                Icon(
                                    painter = painterResource(id = if (displayPassword) R.drawable.outline_visibility_24 else R.drawable.outline_visibility_off_24),
                                    contentDescription = "eye"
                                )
                            }
                        },
                        isError = uiState.loginState == -1 || uiState.loginState == 2,
                        readOnly = uiState.isLoading,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.login()
                            }
                        ),
                        maxLines = 1,
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    SettingItemCard(
                        modifier = Modifier
                    ) {
                        Card(
                            onClick = {
                                viewModel.login()
                            },
                            enabled = !uiState.isLoading,
                            modifier = Modifier.height(50.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.login),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "忘记密码?")
                        }
                    }

                }
            }
            item {
                Text(
                    text = "Tip：本界面登录方式使用河南师范大学智慧校园统一认证系统(与 i 师大 App 相同)。你的账号和密码会被加密储存在本地，不会上传到除学校服务器之外的其他地方。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun LoginWebView(
    navController: NavController,
    viewModel: LoginViewModel,
    changLoginWay: () -> Unit
) {
    var showLogTipDialog by remember {
        mutableStateOf(true)
    }

    val url = "https://authserver2.htu.edu.cn/authserver/login?"
    val headers =
        mapOf("User-Agent" to "Mozilla/5.0 (Linux; Android 13; SM-A037U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36")

    WebView(
        navController = navController,
        url = url,
        initTitle = R.string.login,
        headers = headers
    ) {
        ExtendedFloatingActionButton(
            text = { Text(text = "刷新登录") },
            icon = { Icon(imageVector = Icons.Default.Refresh, contentDescription = "") },
            onClick = { viewModel.changeLogSuccess(true) },
        )
        /*TextButton(onClick = { viewModel.changeLogSuccess(true) }) {
            Text(text = stringResource(id = R.string.login))
        }
        IconButton(onClick = { showLogTipDialog = true }) {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = "ins")
        }
        IconButton(onClick = { viewModel.changeLogSuccess(true) }) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "login")
        }*/
    }
    LogTipDialog(
        showDialog = showLogTipDialog,
        onDismissRequests = { changLoginWay() },
        onConfirmRequests = { showLogTipDialog = false }
    )
}