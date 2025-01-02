package com.smart.htu.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun NewsScreen(
    navController: NavHostController,
) {

    val hazeState = remember { HazeState() }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                modifier = Modifier.hazeChild(
                    state = hazeState,
                    style = HazeMaterials.ultraThin()
                ),
                title = { Text(text = "新闻") }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .haze(state = hazeState)
        )  {
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