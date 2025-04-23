package com.smart.htu.screens.application.grade

import android.util.Log
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.smart.htu.R
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.component.BasicBottomSheet
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SpinnerEntry
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSpinner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTermBottomSheet(
    globalTermCode: String,
    termSelectedCode: String,
    termList: List<SingleTerm>,
    isBottomSheetShow: MutableState<Boolean>,
    onClick: (String) -> Unit,
    otherContent: (LazyListScope.() -> Unit)? = null
) {
    Log.i("TAG666", "termSelectedCode: $termList")
    BasicBottomSheet(
        showDialog = isBottomSheetShow,
        title = stringResource(id = R.string.setting)
    ) {
        SuperDropdown(
            title = "选择学期",
            items = termList.map {
                if (it.termCode == globalTermCode) "${it.termString} (现在)"
                else it.termString
            },
            selectedIndex = termList.map { it.termCode }.indexOf(termSelectedCode),
            onSelectedIndexChange = { onClick(termList[it].termCode) },
            mode = DropDownMode.AlwaysOnRight
        )
    }
}