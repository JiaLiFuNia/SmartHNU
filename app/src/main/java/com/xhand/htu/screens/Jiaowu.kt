package com.xhand.htu.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JiaowuScreen() {
    Column(modifier = Modifier) {
        TopAppBar(
            title = { Text("教务") }
        )
    }
}

@Preview
@Composable
fun JiaowuScreenPreview() {
    JiaowuScreen()
}