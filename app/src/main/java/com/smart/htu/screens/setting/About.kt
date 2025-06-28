package com.smart.htu.screens.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.appIcon
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.GITHUB_PERSON_URL
import com.smart.htu.utils.Constants.Companion.GITHUB_PROJECT_URL
import com.smart.htu.utils.Constants.Companion.SMH_URL
import com.smart.htu.utils.startWebUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About(
    navController: NavController
) {
    var showConfetti by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        showConfetti = true
        scope.launch {
            delay(10000)
            showConfetti = false
        }
    }
    val party = remember {
        Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = { Text(text = stringResource(id = R.string.about)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .overScrollVertical(),
            overscrollEffect = null
        ) {
            item {
                Column(
                    modifier = Modifier
                        .height(320.dp)
                        .fillParentMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        pressFeedbackType = PressFeedbackType.Tilt,
                        showIndication = true,
                        cornerRadius = 40.dp,
                        color = Color.Transparent,
                        modifier = Modifier
                            .size(160.dp)
                    ) {
                        Image(
                            imageVector = DrawableVectors.appIcon(),
                            contentDescription = "app_logo",
                            modifier = Modifier
                                .size(160.dp)
                        )
                    }
                    Text(
                        text = "师韵-SmartHNU",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(R.string.developer),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        leftAction = {
                            Box(
                                contentAlignment = Alignment.TopStart,
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.developer_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                )
                            }
                        },
                        title = stringResource(id = R.string.developer_name),
                        summary = stringResource(id = R.string.developer_description),
                        onClick = {
                            startWebUrl(GITHUB_PERSON_URL)
                        }
                    )
                    SuperArrow(
                        title = stringResource(R.string.project_address),
                        onClick = {
                            startWebUrl(GITHUB_PROJECT_URL)
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(R.string.other),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = stringResource(R.string.official_website),
                        onClick = {
                            navController.navigateToWebView(
                                url = SMH_URL,
                                label = context.getString(R.string.app_name)
                            )
                        }
                    )
                    SuperArrow(
                        title = stringResource(id = R.string.open_source_license),
                        onClick = {
                            navController.navigate(Destinations.License.route)
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(R.string.acknowledgement),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = "HFUT-Schedule",
                        summary = "@Chiu-xaH",
                        onClick = {
                            startWebUrl("https://github.com/Chiu-xaH/HFUT-Schedule")
                        }
                    )
                    SuperArrow(
                        title = "GongYun-for-Android",
                        summary = "@jayfunc",
                        onClick = {
                            startWebUrl("https://github.com/jayfunc/GongYun-for-Android")
                        }
                    )
                    SuperArrow(
                        title = "ReadYou",
                        summary = "@Ashinch",
                        onClick = {
                            startWebUrl("https://github.com/Ashinch/ReadYou")
                        }
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (showConfetti) {
            KonfettiView(
                parties = listOf(party),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}