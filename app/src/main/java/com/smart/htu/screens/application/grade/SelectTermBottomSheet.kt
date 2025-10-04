package com.smart.htu.screens.application.grade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.smart.htu.R
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.component.SuperSpinnerDialog
import top.yukonga.miuix.kmp.extra.SpinnerEntry

@Composable
fun SelectTermBottomSheet(
    globalTermCode: String,
    termSelectedCode: String,
    termList: List<SingleTerm>,
    isBottomSheetShow: MutableState<Boolean>,
    onClick: (String) -> Unit
) {
    val globalTermIndex = termList.indexOfFirst { it.termCode == globalTermCode }
    val filteredList = if (globalTermIndex != -1) {
        val endIndex = (globalTermIndex + 1).coerceAtMost(termList.size - 1)
        termList.subList(0, endIndex + 1)
    } else {
        termList
    }
    SuperSpinnerDialog(
        items = filteredList.map { it.termString + if (it.termCode == globalTermCode) " (现在)" else "" }
            .map { SpinnerEntry(title = it) },
        selectedIndex = termList.indexOfFirst { it.termCode == termSelectedCode }
            .coerceAtLeast(0),
        title = "选择学期",
        dialogButtonString = stringResource(id = R.string.cancel),
        modifier = androidx.compose.ui.Modifier,
        isDropdownExpanded = isBottomSheetShow,
        hapticFeedback = androidx.compose.ui.platform.LocalHapticFeedback.current,
        onSelectedIndexChange = {
            onClick(termList[it].termCode)
        }
    )
}