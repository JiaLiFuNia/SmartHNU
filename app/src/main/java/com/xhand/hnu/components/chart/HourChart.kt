package com.xhand.hnu.components.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.xhand.hnu.model.entity.HourListEntity

@Composable
fun HourChart(hourLists: List<HourListEntity>) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val hourList: List<Number> = hourLists.map { it.total }
    val xLabel = hourLists.map { it.term }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(y = hourList)
            }
        }
    }
    val bottomAxisValueFormatter = CartesianValueFormatter { x, _, _ ->
        xLabel[x.toInt() % xLabel.size]
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(valueFormatter = bottomAxisValueFormatter),
        ),
        modelProducer,
    )
}