package com.smart.htu.screens.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.smart.htu.App.Companion.context
import com.smart.htu.BuildConfig
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.blend.ColorBlendToken
import com.smart.htu.component.effect.BgEffectBackground
import com.smart.htu.component.imageVectors.appIcon
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.ui.theme.isInDarkTheme
import com.smart.htu.utils.APPVersion.getVersionCode
import com.smart.htu.utils.APPVersion.getVersionName
import com.smart.htu.utils.Constants.Companion.GITHUB_PERSON_URL
import com.smart.htu.utils.Constants.Companion.GITHUB_PROJECT_URL
import com.smart.htu.utils.Constants.Companion.SMH_URL
import com.smart.htu.utils.startWebUrl
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurBlendMode
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import androidx.compose.ui.graphics.BlendMode as ComposeBlendMode

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About() {
    val navigator = LocalNavigator.current
    /*var showConfetti by remember { mutableStateOf(false) }
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
    }*/
    val scrollBehavior = MiuixScrollBehavior()
    val lazyListState = rememberLazyListState()
    val scrollProgress by remember {
        derivedStateOf {
            when {
                lazyListState.firstVisibleItemIndex > 0 -> 1f

                else -> {
                    val spacer =
                        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == "logoSpacer" }
                    if (spacer != null && spacer.size > 0) {
                        (lazyListState.firstVisibleItemScrollOffset.toFloat() / spacer.size).coerceIn(
                            0f,
                            1f
                        )
                    } else {
                        0f
                    }
                }
            }
        }
    }
    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null && scrollProgress == 1f
    val barColor = if (blurActive) {
        Color.Transparent
    } else {
        if (scrollProgress == 1f) MiuixTheme.colorScheme.surface else Color.Transparent
    }

    Scaffold(
        topBar = {
            BlurredBar(Modifier, backdrop, blurActive) {
                SmallTopAppBar(
                    title = stringResource(id = R.string.about),
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    titleColor = MiuixTheme.colorScheme.onSurface.copy(
                        alpha = ((scrollProgress - 0.35f) / 0.65f).coerceIn(0f, 1f),
                    ),
                    defaultWindowInsetsPadding = false,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    },
                )
            }
        }
    ) {
        Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
            AboutContent(
                paddingValues = it,
                scrollBehavior = scrollBehavior,
                scrollProgress = scrollProgress,
                lazyListState = lazyListState,
            )
        }
    }
    /*Box(modifier = Modifier.fillMaxSize()) {
        if (showConfetti) {
            KonfettiView(
                parties = listOf(party),
                modifier = Modifier.fillMaxSize()
            )
        }
    }*/
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun AboutContent(
    paddingValues: PaddingValues,
    scrollBehavior: ScrollBehavior,
    scrollProgress: Float,
    lazyListState: LazyListState,
) {
    val navigator = LocalNavigator.current

    val backdrop = rememberBlurBackdrop(true)
    var isOs3Effect by remember { mutableStateOf(true) }
    var showTextureSet by remember { mutableStateOf(false) }
    var blurRadius by remember { mutableFloatStateOf(60f) }
    var noiseCoefficient by remember { mutableFloatStateOf(BlurDefaults.NoiseCoefficient) }
    var brightness by remember { mutableFloatStateOf(0f) }
    var contrast by remember { mutableFloatStateOf(1f) }
    var saturation by remember { mutableFloatStateOf(1f) }

    val isInDark = isInDarkTheme()
    val dynamicBackground = remember { mutableStateOf(isRuntimeShaderSupported()) }
    val isFullScreenBackground = remember { mutableStateOf(true) }

    val cardBlend =
        if (isInDark) ColorBlendToken.Overlay_Thin_Light else ColorBlendToken.Pured_Regular_Light
    val logoBlend = remember(isInDark) {
        if (isInDark) {
            listOf(
                BlendColorEntry(Color(0xe6a1a1a1), BlurBlendMode.ColorDodge),
                BlendColorEntry(Color(0x4de6e6e6), BlurBlendMode.LinearLight),
                BlendColorEntry(Color(0xff1af500), BlurBlendMode.Lab),
            )
        } else {
            listOf(
                BlendColorEntry(Color(0xcc4a4a4a), BlurBlendMode.ColorBurn),
                BlendColorEntry(Color(0xff4f4f4f), BlurBlendMode.LinearLight),
                BlendColorEntry(Color(0xff1af200), BlurBlendMode.Lab),
            )
        }
    }

    val density = LocalDensity.current
    var logoHeightDp by remember { mutableStateOf(300.dp) }

    val versionCodeProgress = ((scrollProgress - 0.05f) / 0.15f).coerceIn(0f, 1f)
    val projectNameProgress = ((scrollProgress - 0.20f) / 0.15f).coerceIn(0f, 1f)
    val iconProgress = ((scrollProgress - 0.35f) / 0.15f).coerceIn(0f, 1f)

    BgEffectBackground(
        dynamicBackground = dynamicBackground.value,
        isOs3Effect = isOs3Effect,
        isFullSize = isFullScreenBackground.value,
        modifier = Modifier.fillMaxSize(),
        bgModifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier,
        alpha = { 1f - scrollProgress },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = paddingValues.calculateTopPadding() + 52.dp + 40.dp,
                    start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateRightPadding(LayoutDirection.Ltr),
                )
                .onSizeChanged { size ->
                    with(density) { logoHeightDp = size.height.toDp() }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(40.dp)
                        alpha = 1 - iconProgress
                        scaleX = 1 - (iconProgress * 0.05f)
                        scaleY = 1 - (iconProgress * 0.05f)
                    }
                    .background(MiuixTheme.colorScheme.surfaceContainer),
            ) {
                Image(
                    modifier = Modifier
                        .size(140.dp),
                    imageVector = appIcon(),
                    contentDescription = null,
                )
            }

            Text(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 5.dp)
                    .graphicsLayer {
                        alpha = 1 - projectNameProgress
                        scaleX = 1 - (projectNameProgress * 0.05f)
                        scaleY = 1 - (projectNameProgress * 0.05f)
                    }
                    .then(
                        if (backdrop != null) {
                            Modifier
                                .textureBlur(
                                    backdrop = backdrop,
                                    shape = RoundedCornerShape(16.dp),
                                    blurRadius = 150f,
                                    noiseCoefficient = noiseCoefficient,
                                    colors = BlurColors(
                                        blendColors = logoBlend,
                                    ),
                                    contentBlendMode = ComposeBlendMode.DstIn,
                                )
                        } else {
                            Modifier
                        },
                    ),
                text = "师韵-SmartHNU",
                color = MiuixTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = paddingValues.calculateTopPadding() + 8.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            state = lazyListState,
            overscrollEffect = null
        ) {
            item(key = "logoSpacer") {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(
                            logoHeightDp + 52.dp + paddingValues.calculateTopPadding() + 40.dp - paddingValues.calculateTopPadding() + 126.dp,
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                showTextureSet = true
                            }
                        },
                    contentAlignment = Alignment.TopCenter,
                    content = { },
                )
            }
            item {
                SettingItemCard(
                    label = stringResource(R.string.developer),
                    backdrop = backdrop,
                    blurRadius = blurRadius,
                    noiseCoefficient = noiseCoefficient,
                    cardBlend = cardBlend,
                    brightness = brightness,
                    contrast = contrast,
                    saturation = saturation,
                    modifier = Modifier
                ) {
                    ArrowPreference(
                        startAction = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                            ) {
                                AsyncImage(
                                    model = "https://avatars.githubusercontent.com/u/69774586?v=4",
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                )
                            }
                        },
                        title = stringResource(id = R.string.developer_description),
                        summary = stringResource(id = R.string.developer_name),
                        onClick = {
                            startWebUrl(GITHUB_PERSON_URL)
                        }
                    )
                    ArrowPreference(
                        title = stringResource(R.string.project_address),
                        onClick = {
                            startWebUrl(GITHUB_PROJECT_URL)
                        }
                    )
                    ArrowPreference(
                        title = stringResource(R.string.official_website),
                        onClick = {
                            navigator.pushWebView(
                                url = SMH_URL,
                                title = context.getString(R.string.app_name)
                            )
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = "应用",
                    backdrop = backdrop,
                    blurRadius = blurRadius,
                    noiseCoefficient = noiseCoefficient,
                    cardBlend = cardBlend,
                    brightness = brightness,
                    contrast = contrast,
                    saturation = saturation,
                    modifier = Modifier
                ) {
                    val clickCount = remember { mutableIntStateOf(0) }
                    BasicComponent(
                        title = "版本",
                        summary = "${getVersionName()} (${getVersionCode()})",
                        onClick = {
                            clickCount.intValue++
                            if (clickCount.intValue >= 3) {
                                clickCount.intValue = 0
                                navigator.push(Route.EmojiEasterEgg)
                            }
                        }
                    )
                    BasicComponent(
                        title = "构建时间",
                        summary = BuildConfig.BUILD_TIME
                    )
                    ArrowPreference(
                        title = stringResource(id = R.string.open_source_license),
                        onClick = {
                            navigator.push(Route.License)
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(R.string.acknowledgement),
                    backdrop = backdrop,
                    blurRadius = blurRadius,
                    noiseCoefficient = noiseCoefficient,
                    cardBlend = cardBlend,
                    brightness = brightness,
                    contrast = contrast,
                    saturation = saturation,
                    modifier = Modifier
                ) {
                    ArrowPreference(
                        title = "HFUT-Schedule",
                        summary = "@Chiu-xaH",
                        onClick = {
                            startWebUrl("https://github.com/Chiu-xaH/HFUT-Schedule")
                        }
                    )
                    ArrowPreference(
                        title = "GongYun-for-Android",
                        summary = "@jayfunc",
                        onClick = {
                            startWebUrl("https://github.com/jayfunc/GongYun-for-Android")
                        }
                    )
                    ArrowPreference(
                        title = "ReadYou",
                        summary = "@Ashinch",
                        onClick = {
                            startWebUrl("https://github.com/Ashinch/ReadYou")
                        }
                    )
                    ArrowPreference(
                        title = "jxh_next",
                        summary = "@paditianxiu",
                        onClick = {
                            startWebUrl("https://github.com/paditianxiu/jxh_next")
                        }
                    )
                }
            }
        }
    }
}
