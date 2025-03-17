package com.tinkooladik.macrobenchmark.chart

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tinkooladik.macrobenchmark.chart.views.BenchmarkChart
import com.tinkooladik.macrobenchmark.chart.views.DragAndDropFile
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.File

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
                var before by remember { mutableStateOf<File?>(null) }
                var after by remember { mutableStateOf<File?>(null) }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    DragAndDropFile(
                        text = "Before"
                    ) { file ->
                        before = file
                    }

                    DragAndDropFile(
                        text = "After"
                    ) { file ->
                        after = file
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                BenchmarkChart(beforeFile = before, afterFile = after)
            }

            // Add a vertical scrollbar
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState),
                modifier = Modifier.fillMaxHeight().padding(end = 8.dp)
            )
        }
    }
}
