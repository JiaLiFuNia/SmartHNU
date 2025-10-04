package com.smart.htu.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.TextButtonWithProgressIndicator
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.HENAN_NORMAL_UNIVERSITY
import com.smart.htu.utils.startLaunchAPK
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Rename
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val autofillManager = LocalAutofillManager.current

    val showLoginInfoDialog = remember { mutableStateOf(false) }

    LaunchedEffect(uiState.jwcLoginState) {
        if (uiState.jwcLoginState == 1) {
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
                    colors = CardDefaults.defaultColors(color = Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.school_logo),
                        contentDescription = "logo",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            item {
                var passwordVisible by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    TextField(
                        value = uiState.studentID,
                        onValueChange = {
                            viewModel.changeStudentID(it)
                        },
                        label = "学号",
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
                            viewModel.changeJWCPassword(it)
                            viewModel.changePassword(it)
                        },
                        label = "智慧教务密码",
                        useLabelAsPlaceholder = true,
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.login(
                                    onSuccess = {
                                        focusManager.clearFocus()
                                        autofillManager?.commit()
                                    }
                                )
                            }
                        ),
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
            item {
                Spacer(modifier = Modifier.height(20.dp))
                TextButtonWithProgressIndicator(
                    text = if (uiState.isLoading) "正在登录..." else "登录",
                    onClick = {
                        if (uiState.studentID == "admin")
                            navController.navigate(Destinations.AccountManage.route)
                        else {
                            viewModel.login(
                                onSuccess = {
                                    focusManager.clearFocus()
                                    autofillManager?.commit()
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    isLoading = uiState.isLoading
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
                    TextButton(
                        onClick = {
                            viewModel.guest()
                            navController.popBackStack()
                        }
                    ) {
                        Text(text = "游客访问")
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
    }
    LoginInfoDialog(
        showDialog = showLoginInfoDialog,
        onDismissRequests = {
            viewModel.showSnackBar("请前往河南师大智慧教务微信公众号进行密码重置")
        }
    )
}