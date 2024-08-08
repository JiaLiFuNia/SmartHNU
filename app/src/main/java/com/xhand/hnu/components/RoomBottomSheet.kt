package com.xhand.hnu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xhand.hnu.viewmodel.CourseSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomBottomSheet(viewModel: CourseSearchViewModel, room: String) {
    val bottomSheetState = rememberModalBottomSheetState()
    val scrollState = rememberScrollState()
    ModalBottomSheet(
        onDismissRequest = { viewModel.showRoomSheet = false },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = room,
                style = MaterialTheme.typography.headlineSmall
            )
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center
            ) {
                if (viewModel.isGettingCourse) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else
                    if (viewModel.searchResult.isEmpty())
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "今天这个教室很空...", color = Color.Gray)
                        }
                    else {
                        viewModel.searchResult.sortBy { it.jcdm.toInt() }
                        viewModel.searchResult.forEach { room ->
                            CourseSearchListItem(course = room)
                        }
                    }

            }
        }
    }
}