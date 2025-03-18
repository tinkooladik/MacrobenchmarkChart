package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import com.tinkooladik.macrobenchmark.chart.captureComposableAsImage
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macrobenchmark.chart.models.toCombinedChartItems
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent(before: BenchmarkReportUi, after: BenchmarkReportUi) {
    val combinedItems by remember {
        derivedStateOf {
            toCombinedChartItems(before, after)
        }
    }
    var selectedBenchmark by remember { mutableStateOf(combinedItems.keys.first()) }

    var columnWidth by remember { mutableStateOf(0) }
    var columnHeight by remember { mutableStateOf(0) }

    val content: @Composable () -> Unit = {
        combinedItems[selectedBenchmark]?.let {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(Color(0xFFFFFFFF))
                    .padding(20.dp)
                    .onGloballyPositioned { coordinates ->
                        columnWidth = coordinates.size.width
                        columnHeight = coordinates.size.height
                    }
            ) {
                Text(
                    selectedBenchmark,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(Color(0xFFCFF9FF))
                        .padding(12.dp)
                )
                BenchmarkChart(it)
            }
        }
    }

    // capture button
    Button(onClick = {
        // capture all items:
        combinedItems.keys.toList().forEach { key ->
            selectedBenchmark = key
            captureComposableAsImage(
                width = columnWidth,
                height = columnHeight,
                title = key
            ) {
                content()
                LaunchedEffect(selectedBenchmark) {
                    delay(200)
                }
            }
        }
        // capture single item:
//        captureComposableAsImage(
//            width = columnWidth,
//            height = columnHeight,
//            title = selectedBenchmark
//        ) {
//            content()
//        }
    }) {
        Text("Save report to clipboard")
    }

    Spacer(modifier = Modifier.height(20.dp))

    // filter chips
    LazyRow {
        items(combinedItems.keys.toList()) { key ->
            FilterChip(
                selected = key == selectedBenchmark,
                onClick = { selectedBenchmark = key },
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(key)
            }
        }
    }

    // content
    content()
}
