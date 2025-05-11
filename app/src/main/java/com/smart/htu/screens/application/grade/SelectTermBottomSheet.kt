package com.smart.htu.screens.application.grade

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.component.BasicBottomSheet
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SpinnerEntry
import top.yukonga.miuix.kmp.extra.SpinnerItemImpl

@Composable
fun SelectTermBottomSheet(
    globalTermCode: String,
    termSelectedCode: String,
    termList: List<SingleTerm>,
    isBottomSheetShow: MutableState<Boolean>,
    onClick: (String) -> Unit
) {
    BasicBottomSheet(
        showDialog = isBottomSheetShow,
        title = stringResource(id = R.string.select_term),
        insideMargin = DpSize(0.dp, 24.dp)
    ) {
        val globalTermIndex = termList.indexOfFirst { it.termCode == globalTermCode }
        val filteredList = if (globalTermIndex != -1) {
            val endIndex = (globalTermIndex + 1).coerceAtMost(termList.size - 1)
            termList.subList(0, endIndex + 1)
        } else {
            termList
        }

        val items = filteredList.map {
            SpinnerEntry(
                title = it.termString + if (it.termCode == globalTermCode) " (现在)" else ""
            )
        }

        val selectedIndex = remember {
            mutableIntStateOf(filteredList.indexOfFirst { it.termCode == termSelectedCode })
        }

        LazyColumn {
            itemsIndexed(items = items) { index, item ->
                SpinnerItemImpl(
                    entry = item,
                    entryCount = items.size,
                    isSelected = index == selectedIndex.intValue,
                    index = index,
                    dialogMode = true,
                    onSelectedIndexChange = {
                        selectedIndex.intValue = index
                    }
                )
            }
        }
        TextButton(
            modifier = Modifier
                .padding(start = 24.dp, top = 12.dp, end = 24.dp)
                .fillMaxWidth(),
            text = "确定",
            minHeight = 50.dp,
            onClick = {
                onClick(filteredList[selectedIndex.intValue].termCode)
                isBottomSheetShow.value = false
            }
        )
    }
}