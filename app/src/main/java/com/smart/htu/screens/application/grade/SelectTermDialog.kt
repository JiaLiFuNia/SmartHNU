package com.smart.htu.screens.application.grade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.smart.htu.api.module.SingleTerm
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.RadioButtonPreference

@Composable
fun SelectTermDialog(
    globalTermCode: String,
    termSelectedCode: String,
    termList: List<SingleTerm>,
    show: Boolean,
    onClick: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val globalTermIndex = termList.indexOfFirst { it.termCode == globalTermCode }
    val filteredList = if (globalTermIndex != -1) {
        val endIndex = (globalTermIndex + 1).coerceAtMost(termList.size - 1)
        termList.subList(0, endIndex + 1)
    } else {
        termList
    }
    OverlayDialog(
        title = "选择学期",
        show = show,
        onDismissRequest = onDismissRequest,
        insideMargin = DpSize(0.dp, 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            filteredList.map { it.termString + if (it.termCode == globalTermCode) " (现在)" else "" }
                .forEachIndexed { index, it ->
                    RadioButtonPreference(
                        title = it,
                        selected = termList[index].termCode == termSelectedCode,
                        onClick = {
                            onClick(termList[index].termCode)
                            onDismissRequest()
                        }
                    )
                }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 24.dp)
            ) {
                TextButton(
                    text = "取消",
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}