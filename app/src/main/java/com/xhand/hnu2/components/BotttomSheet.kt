package com.xhand.hnu2.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    showModalBottomSheet: MutableState<Boolean>,
    text: String,
    content: (@Composable () -> Unit)? = null,
) {
    val bottomSheetState = rememberModalBottomSheetState()
    if (showModalBottomSheet.value)
        ModalBottomSheet(
            windowInsets = WindowInsets.navigationBars,
            onDismissRequest = { showModalBottomSheet.value = !showModalBottomSheet.value },
            sheetState = bottomSheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (content != null) {
                        content()
                    }
                }
            }
        }
}

