package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.tinkooladik.macrobenchmark.chart.captureComposableAsImage
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macrobenchmark.chart.models.toCombinedChartItems

@Composable
fun MainContent(before: BenchmarkReportUi, after: BenchmarkReportUi) {
    val combinedItems by remember {
        derivedStateOf {
            toCombinedChartItems(before, after)
        }
    }
    var columnWidth by remember { mutableStateOf(0) }
    var columnHeight by remember { mutableStateOf(0) }

    Button(onClick = {
        captureComposableAsImage(
            width = columnWidth,
            height = columnHeight
        ) {
            BenchmarkChart(combinedItems)
        }
    }) {
        Text("Save report to clipboard")
    }

    BenchmarkChart(
        combinedItems,
        Modifier.onGloballyPositioned { coordinates ->
            columnWidth = coordinates.size.width
            columnHeight = coordinates.size.height
        })
}