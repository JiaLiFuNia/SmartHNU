package com.smart.htu.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.textButtonPrimaryColors
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.RETRIEVE_PASSWORD
import com.smart.htu.utils.startLaunchAPK
import com.smart.htu.utils.startWebUrl
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
    val focusManager = LocalFocusManager.current
    val autofillManager = LocalAutofillManager.current

    var displayPassword by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    displayPassword = isPressed

    LaunchedEffect(uiState.loginState, uiState.loginJWCState) {
        if (uiState.loginState + uiState.loginJWCState == 2) {
            navController.popBackStack()
        }
    }

    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background,
                ),
                title = {
                    Text(text = "师韵 登录")
                },
                actions = {
                    TextButton(onClick = { startLaunchAPK("com.autewifi.sd.enroll") }) {
                        Text(text = "i 师大")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.school_logo),
                    contentDescription = "logo",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            }
            // Color(90,158,157) 师大绿
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "account",
                            tint = if (uiState.loginState == -1 || uiState.studentID.length > 10)
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                    },
                    value = uiState.studentID,
                    onValueChange = {
                        viewModel.changeStudentID(it)
                    },
                    label = { Text(text = "学号") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentType = ContentType.Username },
                    isError = uiState.loginState == -1 || uiState.studentID.length > 10,
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
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.key_24px),
                            contentDescription = "key",
                            tint = if (uiState.loginState == -1)
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                    },
                    value = uiState.password,
                    onValueChange = {
                        viewModel.changePassword(it)
                    },
                    label = { Text(text = "统一认证密码") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentType = ContentType.Password },
                    visualTransformation = if (displayPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        when (uiState.loginState) {
                            -1 -> {
                                Icon(
                                    painter = painterResource(id = R.drawable.warning_24px),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                            0 -> {
                                IconButton(onClick = {
                                    displayPassword = !displayPassword
                                }) {
                                    Icon(
                                        painter = painterResource(id = if (displayPassword) R.drawable.outline_visibility_24 else R.drawable.outline_visibility_off_24),
                                        contentDescription = "eye"
                                    )
                                }
                            }

                            1 -> {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    isError = uiState.loginState == -1,
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
                    singleLine = true,
                    maxLines = 1,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.key_24px),
                            contentDescription = "key",
                            tint = if (uiState.loginJWCState == -1)
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                    },
                    value = uiState.jwcPassword,
                    onValueChange = {
                        viewModel.changeJWCPassword(it)
                    },
                    label = { Text(text = "智慧教务密码") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentType = ContentType.Password },
                    visualTransformation = if (displayPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        when (uiState.loginJWCState) {
                            -1 -> {
                                Icon(
                                    painter = painterResource(id = R.drawable.warning_24px),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                            0 -> {
                                IconButton(onClick = {
                                    displayPassword = !displayPassword
                                }) {
                                    Icon(
                                        painter = painterResource(id = if (displayPassword) R.drawable.outline_visibility_24 else R.drawable.outline_visibility_off_24),
                                        contentDescription = "eye"
                                    )
                                }
                            }

                            1 -> {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    isError = uiState.loginJWCState == -1,
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
                    singleLine = true,
                    maxLines = 1,
                    shape = RoundedCornerShape(10.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            top.yukonga.miuix.kmp.basic.TextButton(
                text = if (uiState.isLoading) "正在登录..." else "登录",
                onClick = {
                    if (uiState.studentID == "admin")
                        navController.navigate(Destinations.AccountManage.route)
                    else {
                        focusManager.clearFocus()
                        autofillManager?.commit()
                        viewModel.login()
                    }
                },
                colors = ButtonDefaults.textButtonPrimaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
            ) {
                TextButton(onClick = { startWebUrl(RETRIEVE_PASSWORD) }) {
                    Text(text = "忘记密码?")
                }
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(text = "暂不登录")
                }
            }
            Text(
                text = "Tip：\n- 统一认证密码 用于登录河南师范大学智慧校园统一认证系统，与 i 师大登录密码相同。默认密码为：Myhtu+身份证号后七位的前六位。\n\n" +
                        "- 智慧教务密码 用于登录河南师范大学智慧教务，与教务系统密码相同。\n\n" +
                        "- 你的账号和密码会被加密储存在本地。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
            )
        }
    }
}