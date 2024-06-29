package com.xhand.hnu.components.Chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import com.xhand.hnu.model.entity.JDList

@Composable
fun GPAChangeLineChart(JDdata: List<JDList>) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val gpaList: List<Number> = JDdata.map { it.pjxfjd.toFloat() }
    LaunchedEffect(Unit) {
        modelProducer.tryRunTransaction {
            lineSeries {
                series(gpaList)
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
        ),
        modelProducer = modelProducer,
    )
}
