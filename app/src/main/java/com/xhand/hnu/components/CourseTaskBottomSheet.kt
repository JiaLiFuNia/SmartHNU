package com.xhand.hnu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xhand.hnu.repository.Repository
import com.xhand.hnu.viewmodel.CourseTaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet(courseTaskViewModel: CourseTaskViewModel) {
    val bottomSheetState = rememberModalBottomSheetState()
    val term = Repository.getCurrentTerm()
    val longGradeTerm = term.longGradeTerm

    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = { courseTaskViewModel.showBookSelect = false },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "选择学期",
                style = MaterialTheme.typography.headlineSmall
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                longGradeTerm.forEachIndexed { index, term ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = longGradeTerm[courseTaskViewModel.selectTerm] == term,
                                onClick = { courseTaskViewModel.selectTerm = index }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = longGradeTerm[courseTaskViewModel.selectTerm] == term,
                            onClick = null
                        )
                        Text(
                            text = term,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}