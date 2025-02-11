package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicBottomSheet(
    isBottomSheetShow: Boolean,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (isBottomSheetShow)
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onConfirmClick != null) {
                        IconButton(onClick = { onDismissRequest() }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "close"
                            )
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    if (onConfirmClick != null) {
                        IconButton(onClick = { onConfirmClick() }) {
                            Icon(
                                imageVector = Icons.Outlined.Done,
                                contentDescription = "save"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier) {
                    content()
                }
            }
        }
}