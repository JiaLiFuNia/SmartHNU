package com.xhand.hnu.components.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.xhand.hnu.model.entity.JDList
import com.xhand.hnu.viewmodel.GradeViewModel

@Composable
fun GPAChangeLineChart(jdLists: List<JDList>, gradeViewModel: GradeViewModel) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val gpaList: List<Number> = jdLists.map { it.pjxfjd.toFloat() }
    val xLabel = gradeViewModel.convertTermToIndex(jdLists.map { it.mc }.toMutableList())
    val bottomAxisValueFormatter = CartesianValueFormatter { x, _, _ ->
        xLabel[x.toInt() % xLabel.size]
    }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(y = gpaList)
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = rememberStartAxis(            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = bottomAxisValueFormatter
            ),
        ),
        modelProducer = modelProducer,
    )
}
