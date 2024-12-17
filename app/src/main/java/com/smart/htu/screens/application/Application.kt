package com.smart.htu.screens.application

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.smart.htu.component.SmallMediumCardDisplay
import com.smart.htu.screens.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Application(
    navController: NavHostController,
    viewModel: ApplicationViewModel,
    mainViewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = "",
                    onQueryChange = { },
                    onSearch = { },
                    expanded = false,
                    onExpandedChange = { },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "search"
                        )
                    },
                    trailingIcon = {
                        FilledTonalIconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = "add")
                        }
                    },
                    placeholder = {
                        Text(text = "搜索应用...")
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 15.dp)
        ) {
            items(uiState.appList.size) { item ->
                SmallMediumCardDisplay(
                    content = uiState.appList[item],
                    navController = navController,
                    modifier = Modifier
                        .padding(5.dp),
                    onLongClick = {
                        viewModel.changeCommonAppListState(item)
                    },
                    addToCommonClick = !uiState.appListIsCommonList.contains(uiState.appList[item])
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
