package com.smart.htu.screens.application.grade

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.component.BasicBottomSheet
import com.smart.htu.component.PreferenceSubtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTermBottomSheet(
    termSelectedCode: String,
    termList: List<SingleTerm>,
    isBottomSheetShow: Boolean,
    onDismissRequest: () -> Unit,
    onClick: (String) -> Unit,
    otherContent: (LazyListScope.() -> Unit)? = null
) {
    Log.i("TAG666", "termSelectedCode: $termList")
    BasicBottomSheet(
        isBottomSheetShow = isBottomSheetShow,
        title = stringResource(id = R.string.setting),
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(12.dp)
        ) {
            item {
                PreferenceSubtitle(text = "学年学期")
                termList.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = termSelectedCode == it.termCode,
                                onClick = {
                                    onClick(it.termCode)
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = termSelectedCode == it.termCode,
                            onClick = null
                        )
                        Text(
                            text = it.termString,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
            if (otherContent != null) {
                otherContent()
            }
        }
    }
}