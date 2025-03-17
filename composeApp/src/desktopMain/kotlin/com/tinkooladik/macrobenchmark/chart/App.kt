package com.tinkooladik.macrobenchmark.chart

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macrobenchmark.chart.models.toCombinedChartItems
import com.tinkooladik.macrobenchmark.chart.views.BenchmarkChart
import com.tinkooladik.macrobenchmark.chart.views.UploadFiles
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scrollState = rememberScrollState()
        Box(
            Modifier.fillMaxSize()
                .background(Color(0xFFFFFFFF))
        ) {
            Column(
                Modifier.fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var before by remember { mutableStateOf<BenchmarkReportUi?>(null) }
                var after by remember { mutableStateOf<BenchmarkReportUi?>(null) }

                UploadFiles(
                    onUploadedBefore = { before = it },
                    onUploadedAfter = { after = it }
                )

                if (before != null && after != null) {
                    val combinedItems by remember {
                        derivedStateOf {
                            toCombinedChartItems(before!!, after!!)
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
            }

            // Add a vertical scrollbar
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState),
                modifier = Modifier.fillMaxHeight().padding(end = 8.dp)
            )
        }
    }
}
