package com.smart.htu.screens.application.classroom

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.smart.htu.api.module.CourseInfoEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.screens.application.courseSearch.CourseSearchResItem
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalDate

@Composable
fun SingleRoom(
    label: String,
    formerPeriodBusyState: Boolean,
    latterPeriodBusyState: Boolean,
    onClick: (() -> Unit)? = null,
    onAddTaskClick: (() -> Unit)? = null,
    onRemoveTaskClick: (() -> Unit)? = null,
    occupationDetail: List<CourseInfoEntity>?,
    date: LocalDate? = null,
    modifier: Modifier
) {
    val isOccupationDetailBottomSheetShow = remember { mutableStateOf(false) }
    Surface(
        shape = RoundedCornerShape(CardDefaults.CornerRadius),
        modifier = modifier
            .height(50.dp),
        color = MiuixTheme.colorScheme.surfaceContainer,
        onClick = {
            if (onClick != null) {
                onClick()
                isOccupationDetailBottomSheetShow.value = true
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            color = if (formerPeriodBusyState) MiuixTheme.colorScheme.disabledSecondaryVariant
                            else MiuixTheme.colorScheme.surfaceContainer
                        )
                )
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            color = if (latterPeriodBusyState) MiuixTheme.colorScheme.disabledSecondaryVariant
                            else MiuixTheme.colorScheme.surfaceContainer
                        )
                )
            }
            Text(
                text = label,
                maxLines = 1,
                color = if (formerPeriodBusyState && latterPeriodBusyState) MiuixTheme.colorScheme.disabledOnSurface
                else MiuixTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .basicMarquee(
                        repeatDelayMillis = 2_000,
                    )
            )
        }
    }
    OverlayBottomSheet(
        title = "$label ${
            date?.let {
                if (it.isEqual(LocalDate.now())) "今天"
                else convertLocalDateToStringDate(it, "M月d日 E")
            }
        }的课程",
        show = isOccupationDetailBottomSheetShow.value,
        onDismissRequest = {
            isOccupationDetailBottomSheetShow.value = false
        },
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                        + WindowInsets.captionBar.asPaddingValues().calculateBottomPadding(),
            )
        ) {
            if (occupationDetail == null) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            } else {
                if (occupationDetail.isEmpty()) {
                    item {
                        EmptyContent(
                            text = "没有课程", modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                } else {
                    items(occupationDetail.sortedBy { it.sectionCode }) {
                        CourseSearchResItem(
                            course = it,
                            onAddClick = {
                                onAddTaskClick?.invoke()
                            },
                            onRemoveClick = {
                                onRemoveTaskClick?.invoke()
                            },
                            isInTaskList = false
                        )
                    }
                }
            }
        }
    }
}