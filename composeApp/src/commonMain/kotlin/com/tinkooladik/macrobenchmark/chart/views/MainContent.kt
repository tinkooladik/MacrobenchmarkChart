package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinkooladik.macrobenchmark.chart.captureComposableAsImage
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macrobenchmark.chart.models.toCombinedChartItems
import com.tinkooladik.macrobenchmark.chart.selectFolder
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

    val density = LocalDensity.current.density

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
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(Color(0xFFCFF9FF))
                        .padding(20.dp)
                )
                BenchmarkChart(it)
            }
        }
    }

    // capture buttons
    Row {
        Button(onClick = {
            captureComposableAsImage(
                width = columnWidth,
                height = columnHeight,
                density = density,
                title = selectedBenchmark
            ) {
                content()
            }
        }) {
            Text("Copy to clipboard")
        }
        Spacer(modifier = Modifier.width(20.dp))
        Button(onClick = {
            // capture all items:
            val folder = selectFolder()?.absolutePath
            combinedItems.keys.toList().forEach { key ->
                selectedBenchmark = key
                captureComposableAsImage(
                    width = columnWidth,
                    height = columnHeight,
                    density = density,
                    folder = folder ?: "selectedFolder",
                    title = key,
                    copyToClipboard = false
                ) {
                    content()
                    LaunchedEffect(selectedBenchmark) {
                        delay(200)
                    }
                }
            }
        }) {
            Text("Save all reports")
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

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
