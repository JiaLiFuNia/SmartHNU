package com.smart.htu.screens.application.grade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.component.BasicDialog
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperDropdown

@Composable
fun SelectTermBottomSheet(
    globalTermCode: String,
    termSelectedCode: String,
    termList: List<SingleTerm>,
    isBottomSheetShow: MutableState<Boolean>,
    onClick: (String) -> Unit
) {
    BasicDialog(
        showDialog = isBottomSheetShow,
        title = stringResource(id = R.string.setting),
        insideMargin = DpSize(24.dp, 24.dp)
    ) {
        val globalTermIndex = termList.indexOfFirst { it.termCode == globalTermCode }
        val filteredList = if (globalTermIndex != -1) {
            val endIndex = (globalTermIndex + 1).coerceAtMost(termList.size - 1)
            termList.subList(0, endIndex + 1)
        } else {
            termList
        }

        val items = filteredList.map { it.termCode }

        SuperDropdown(
            title = "学期",
            items = filteredList.map {
                it.termString + if (it.termCode == globalTermCode) " (现在)" else ""
            },
            selectedIndex = items.indexOf(termSelectedCode),
            onSelectedIndexChange = {
                onClick(items[it])
            },
            mode = DropDownMode.AlwaysOnRight,
            maxHeight = 240.dp
        )
    }
}