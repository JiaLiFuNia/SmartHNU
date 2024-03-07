package com.xhand.hnu2.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xhand.hnu2.components.GradeListItem
import com.xhand.hnu2.model.entity.ArticleListEntity
import com.xhand.hnu2.model.entity.KccjList
import com.xhand.hnu2.viewmodel.GradeViewModel
import com.xhand.hnu2.viewmodel.PersonViewModel
import com.xhand.hnu2.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    if (settingsViewModel.isLoginSuccess) {
        LaunchedEffect(Unit) {
            settingsViewModel.gradeService()
            Log.i("TAG666", "2${settingsViewModel.gradeList}${settingsViewModel.isLoginSuccess}")
            Log.i("TAG666", "1")
        }
    }
    Log.i("TAG666","5656${settingsViewModel.gradeList}")
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(
                            MaterialTheme.colorScheme.surface.copy()
                        )
                ),
                title = {
                    Text(text = "成绩")
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues = it)) {
            Log.i("TAG666", "3")
            items(settingsViewModel.gradeList) { grade ->
                GradeListItem(grade = grade, modifier = Modifier.clickable { })
            }
        }
    }
}
