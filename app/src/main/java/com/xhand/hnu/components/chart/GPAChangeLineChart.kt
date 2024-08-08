package com.xhand.hnu.components.chart

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.decoration.rememberHorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.Shape
import com.xhand.hnu.model.entity.JDList
import com.xhand.hnu.viewmodel.GradeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GPAChangeLineChart(jdLists: List<JDList>, gradeViewModel: GradeViewModel) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val gpaList: List<Number> = jdLists.map { it.pjxfjd.toFloat() }
    val gpaListDouble: List<Double> = jdLists.map { it.pjxfjd.toDouble() }
    val gpaAvg = gpaListDouble.average()
    val xLabel = gradeViewModel.convertTermToIndex(jdLists.map { it.mc }.toMutableList())
    val bottomAxisValueFormatter = CartesianValueFormatter { x, _, _ ->
        xLabel[x.toInt() % xLabel.size]
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {
                lineSeries {
                    series(gpaList)
                }
            }
        }
    }

    val marker = rememberMarker()
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
            ),
            startAxis = rememberStartAxis(
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = bottomAxisValueFormatter,
            ),
            marker = marker,
            persistentMarkers = rememberExtraLambda(marker) { marker at gpaList.size - 1 },
            decorations = listOf(
                rememberHorizontalLine(
                    label = { "AVG:${gpaAvg.toFloat()}" },
                    line = LineComponent(
                        color = MaterialTheme.colorScheme.primary.toArgb(),
                        thicknessDp = 2f
                    ),
                    y = { gpaAvg },
                    labelComponent = TextComponent(
                        padding = Dimensions(8f, 2f),
                        margins = Dimensions(4f),
                        color = MaterialTheme.colorScheme.surface.toArgb(),
                        background = rememberShapeComponent(
                            shape = Shape.Pill,
                            color = MaterialTheme.colorScheme.primary
                        ),
                    )
                )
            )
        ),
        modelProducer = modelProducer,
        zoomState = rememberVicoZoomState(zoomEnabled = false),
    )
    /*LineChart(
        modifier = Modifier
            .fillMaxSize(),
        data = listOf(
            Line(
                label = "Windows",
                values = jdLists.map { it.pjxfjd.toDouble() },
                color = SolidColor(MaterialTheme.colorScheme.primary),
                curvedEdges = true,
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(Color.White),
                    strokeWidth = 2f.dp,
                    radius = 3f.dp,
                    strokeColor = SolidColor(MaterialTheme.colorScheme.primary),
                )
            )
        ),
        curvedEdges = true
    )*/
}