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
import top.yukonga.miuix.kmp.extra.SpinnerEntry
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSpinner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTermBottomSheet(
    termSelectedCode: String,
    termList: List<SingleTerm>,
    isBottomSheetShow: MutableState<Boolean>,
    onClick: (String) -> Unit,
    otherContent: (LazyListScope.() -> Unit)? = null
) {
    val spinnerOptions = termList.map {
        SpinnerEntry(
            title = it.termString,
            summary = it.termCode
        )
    }
    Log.i("TAG666", "termSelectedCode: $termList")
    BasicBottomSheet(
        showDialog = isBottomSheetShow,
        title = stringResource(id = R.string.setting)
    ) {
        SuperSpinner(
            title = "选择学期",
            dialogButtonString = "取消",
            items = spinnerOptions,
            selectedIndex = termList.map { it.termCode }.indexOf(termSelectedCode),
            onSelectedIndexChange = { onClick(termList[it].termCode) },
        )
    }
}