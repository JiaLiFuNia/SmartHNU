package com.smart.htu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.extra.SuperDialogDefaults
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.MiuixPopupUtils.Companion.DialogLayout
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.getRoundedCorner
import top.yukonga.miuix.kmp.utils.getWindowSize
import kotlin.compareTo
import kotlin.div


@Composable
fun BottomCircularProgressIndicator(
    loadingState: MutableState<Boolean>,
    loadingText: String = "正在加载..."
) {
    val currentOutsideMargin by rememberUpdatedState(SuperDialogDefaults.outsideMargin)
    val currentInsideMargin by rememberUpdatedState(SuperDialogDefaults.insideMargin)
    val density = LocalDensity.current
    val getWindowSize by rememberUpdatedState(getWindowSize())
    val windowWidth by rememberUpdatedState(getWindowSize.width.dp / density.density)
    val windowHeight by rememberUpdatedState(getWindowSize.height.dp / density.density)
    val roundedCorner by rememberUpdatedState(getRoundedCorner())
    val paddingModifier =
        remember(currentOutsideMargin) {
            Modifier
                .padding(horizontal = currentOutsideMargin.width)
                .padding(bottom = currentOutsideMargin.height)
        }
    val bottomCornerRadius by remember { derivedStateOf { if (roundedCorner != 0.dp) roundedCorner - currentOutsideMargin.width else 32.dp } }

    val contentAlignment by remember { derivedStateOf { if (windowHeight >= 480.dp && windowWidth >= 840.dp) Alignment.Center else Alignment.BottomCenter } }


    DialogLayout(
        visible = loadingState
    ) {
        Box(
            modifier = Modifier
                .imePadding()
                .navigationBarsPadding()
                .fillMaxSize()
                .then(paddingModifier)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .align(contentAlignment)
                    .clip(shape = SmoothRoundedCornerShape(bottomCornerRadius))
                    .background(
                        color = MiuixTheme.colorScheme.surface,
                    )
                    .padding(
                        horizontal = currentInsideMargin.width,
                        vertical = currentInsideMargin.height
                    )
            ) {
                InfiniteProgressIndicator(
                    modifier = Modifier,
                    color = MiuixTheme.colorScheme.onSurface,
                    size = 24.dp,
                    strokeWidth = 2.dp,
                    orbitingDotSize = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = loadingText,
                    fontSize = 19.sp,
                    color = MiuixTheme.colorScheme.onSurface
                )
            }
        }
    }
}