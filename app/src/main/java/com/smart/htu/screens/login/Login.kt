package com.smart.htu.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.EmptyContent
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.HENAN_NORMAL_UNIVERSITY
import com.smart.htu.utils.startLaunchAPK
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Rename
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val autofillManager = LocalAutofillManager.current

    var displayPassword by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    displayPassword = isPressed

    val showLoginInfoDialog = remember { mutableStateOf(false) }

    LaunchedEffect(uiState.loginJWCState) {
        if (uiState.loginJWCState == 1) {
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
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "close"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackBarHostState)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .overScrollVertical()
                .padding(it),
            overscrollEffect = null,
            contentPadding = PaddingValues(horizontal = 32.dp),
            userScrollEnabled = false
        ) {
            item {
                top.yukonga.miuix.kmp.basic.Card(
                    modifier = Modifier.padding(top = 44.dp, bottom = 56.dp),
                    color = Color.Transparent
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.school_logo),
                        contentDescription = "logo",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            item {
                LoginTextField(
                    viewModel = viewModel,
                    uiState = uiState,
                    title = "智慧教务登录",
                    firstLabel = "学号",
                    secondLabel = "智慧教务密码",
                    onFirstValueChange = {
                        viewModel.changeStudentID(it)
                    },
                    onSecondValueChange = {
                        viewModel.changeJWCPassword(it)
                        viewModel.changePassword(it)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                TextWithProgressIndicatorButton(
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
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            showLoginInfoDialog.value = true
                            /*navController.navigateToWebView(
                                url = RETRIEVE_PASSWORD,
                                label = "忘记密码"
                            )*/
                        }
                    ) {
                        Text(text = "忘记密码?")
                    }
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(text = "暂不登录")
                    }
                }
            }
            item {
                EmptyContent(
                    annotatedText = AnnotatedString(
                        text = "河南师范大学  |  i 师大",
                        annotations = listOf(
                            AnnotatedString.Range(
                                item = LinkAnnotation.Clickable(
                                    tag = "web",
                                    linkInteractionListener = LinkInteractionListener {
                                        navController.navigateToWebView(
                                            url = HENAN_NORMAL_UNIVERSITY,
                                            label = "河南师范大学"
                                        )
                                    }
                                ),
                                start = 0,
                                end = 6
                            ),
                            AnnotatedString.Range(
                                item = LinkAnnotation.Clickable(
                                    tag = "hnu",
                                    linkInteractionListener = LinkInteractionListener {
                                        startLaunchAPK("com.autewifi.sd.enroll")
                                    }
                                ),
                                start = 11,
                                end = 15
                            )
                        )
                    )
                )
            }
        }
        /* {
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
        }*/
    }
    LoginInfoDialog(
        showDialog = showLoginInfoDialog,
        onDismissRequests = {
            viewModel.showSnackBar("请前往河南师大智慧教务微信公众号进行密码重置")
        }
    )
}

@Composable
fun LoginTextField(
    viewModel: LoginViewModel,
    uiState: LoginUiState,
    title: String,
    firstLabel: String = "学号",
    secondLabel: String,
    onFirstValueChange: (String) -> Unit,
    onSecondValueChange: (String) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        /*Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(color = MiuixTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))*/
        TextField(
            value = uiState.studentID,
            onValueChange = {
                onFirstValueChange(it)
            },
            label = firstLabel,
            useLabelAsPlaceholder = true,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.Username },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = uiState.jwcPassword,
            onValueChange = {
                onSecondValueChange(it)
            },
            label = secondLabel,
            useLabelAsPlaceholder = true,
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Icon(
                        imageVector = MiuixIcons.Useful.Rename,
                        tint = if (passwordVisible) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onSecondaryContainer,
                        contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.Password },
        )
    }
}


@Composable
fun TextWithProgressIndicatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = ButtonDefaults.CornerRadius,
    minHeight: Dp = ButtonDefaults.MinHeight,
    insideMargin: PaddingValues = ButtonDefaults.InsideMargin,
) {
    Surface(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = modifier.semantics { role = Role.Button },
        shape = SmoothRoundedCornerShape(cornerRadius),
        color = if (enabled) MiuixTheme.colorScheme.primaryContainer else MiuixTheme.colorScheme.disabledPrimaryButton
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
                .padding(insideMargin),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!enabled) InfiniteProgressIndicator(size = 16.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                textAlign = TextAlign.Center,
                text = text,
                color = if (enabled) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.disabledOnPrimaryButton
            )
        }
    }
}