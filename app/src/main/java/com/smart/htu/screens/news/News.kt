package com.smart.htu.screens.news

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.R
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun NewsScreen(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    // val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            val textFieldState = rememberTextFieldState()
            var expanded by rememberSaveable { mutableStateOf(false) }
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        modifier = Modifier,
                        state = textFieldState,
                        onSearch = { expanded = false },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Hinted search text") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                        colors = inputFieldColors(unfocusedContainerColor = Color.Transparent),
                    )
                },
                colors = SearchBarDefaults.colors(
                    inputFieldColors = inputFieldColors(
                        unfocusedContainerColor = Color.Transparent
                    )
                ),
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {

            }
            LazyColumn(
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 80.dp,
                    bottom = 16.dp
                )
            ) {
                val imageUrls = listOf(
                    "https://www.htu.edu.cn/_upload/article/images/43/cc/d9a635c24f64ba4c40187ffcbdf6/80556644-902c-4848-a367-b74e04cf543e.jpg",
                    "https://www.htu.edu.cn/_upload/article/images/5d/57/2db7f5a74258b3af27efc326fde9/11ee8433-c76f-4a26-bec6-04515aa63c9b.jpg",
                    "https://www.htu.edu.cn/_upload/article/images/eb/a1/2df709514a0a98c646df10b11c9c/bc1f3c83-ab9d-4625-9350-331ca25ef49d.jpg",
                    "https://www.htu.edu.cn/_upload/article/images/a9/10/f8a1d0b549db957ef09d4f27ec8f/996a31c2-1bff-44a0-b742-2b70c8a7acc2.jpg",
                    "https://www.htu.edu.cn/_upload/article/images/6e/4f/48060b494228a7b51256b07d8adb/53df809b-9c3a-4e84-8497-81b5a72b2e7a.jpg"
                )

                item {
                    HorizontalMultiBrowseCarousel(
                        state = rememberCarouselState { imageUrls.count() },
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .fillMaxWidth(),
                        preferredItemWidth = 320.dp,
                        itemSpacing = 4.dp
                    ) { index ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrls[index])
                                .crossfade(true)
                                .addHeader("User-Agent", "Mozilla/5.0")
                                .error(R.drawable.image_placeholder)
                                .build(),
                            contentDescription = "picture",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9f)
                                .maskClip(MaterialTheme.shapes.extraLarge),
                            placeholder = painterResource(id = R.drawable.image_placeholder)
                        )
                    }
                }
                item {
                    val colors = MaterialTheme.colorScheme
                    ColorItem("Primary", colors.primary)
                    ColorItem("PrimaryContainer", colors.primaryContainer)
                    ColorItem("On Primary", colors.onPrimary)
                    ColorItem("on PrimaryContainer", colors.onPrimaryContainer)
                    ColorItem("inversePrimary", colors.inversePrimary)

                    HorizontalDivider()

                    ColorItem("Secondary", colors.secondary)
                    ColorItem("secondaryContainer", colors.secondaryContainer)
                    ColorItem("onSecondary", colors.onSecondary)
                    ColorItem("onSecondaryContainer", colors.onSecondaryContainer)

                    HorizontalDivider()

                    ColorItem("Background", colors.background)
                    ColorItem("On Background", colors.onBackground)

                    HorizontalDivider()

                    ColorItem("Surface", colors.surface)
                    ColorItem("On Surface", colors.onSurface)
                    ColorItem("SurfaceContainer", colors.surfaceContainer)
                    ColorItem("SurfaceContainerHighest", colors.surfaceContainerHighest)
                    ColorItem("SurfaceContainerHigh", colors.surfaceContainerHigh)
                    ColorItem("SurfaceContainerLow", colors.surfaceContainerLow)
                    ColorItem("SurfaceContainerLowest", colors.surfaceContainerLowest)
                    ColorItem("SurfaceBright", colors.surfaceBright)
                    ColorItem("SurfaceDim", colors.surfaceDim)
                    ColorItem("SurfaceTint", colors.surfaceTint)
                    ColorItem("SurfaceVariant", colors.surfaceVariant)
                    ColorItem("SurfaceVariant", colors.onSurfaceVariant)
                    ColorItem("SurfaceVariant", colors.inverseSurface)
                    ColorItem("SurfaceVariant", colors.inverseOnSurface)

                    HorizontalDivider()

                    ColorItem("Error", colors.error)
                    ColorItem("ErrorContainer", colors.errorContainer)
                    ColorItem("On Error", colors.onError)
                    ColorItem("OnErrorContainer", colors.onErrorContainer)

                    HorizontalDivider()

                    ColorItem("Tertiary", colors.tertiary)
                    ColorItem("Tertiary", colors.tertiaryContainer)
                    ColorItem("Tertiary", colors.onTertiary)
                    ColorItem("Tertiary", colors.onTertiaryContainer)

                    HorizontalDivider()

                    ColorItem("Outline", colors.outline)
                    ColorItem("Outline", colors.outlineVariant)

                    HorizontalDivider()

                    ColorItem("Scrim", colors.scrim)
                }
            }
        }

    }
}


@Composable
fun ColorItem(label: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display color box
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = color, shape = RoundedCornerShape(CornerSize(8.dp)))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "$label - ${color.toArgb().toString(16).uppercase()}",
            style = TextStyle(fontSize = 16.sp)
        )
    }
}