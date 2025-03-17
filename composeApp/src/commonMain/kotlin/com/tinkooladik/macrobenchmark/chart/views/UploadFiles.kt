package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tinkooladik.macrobenchmark.chart.json
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReport
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macrobenchmark.chart.models.toUi

@Composable
fun UploadFiles(
    onUploadedBefore: (BenchmarkReportUi) -> Unit,
    onUploadedAfter: (BenchmarkReportUi) -> Unit
) {
    var beforeError by remember { mutableStateOf("") }
    var afterError by remember { mutableStateOf("") }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(
            modifier = Modifier.weight(1f)
                .padding(20.dp)
        ) {
            DragAndDropFile(text = "Before") { file ->
                try {
                    json.decodeFromString<BenchmarkReport>(file.readText()).toUi()
                        .let { onUploadedBefore(it) }
                    beforeError = ""
                } catch (e: Exception) {
                    beforeError = "Error parsing `Before` file: ${e.message}"
                }
            }
            Text(beforeError, color = Color.Red)
        }

        Column(
            modifier = Modifier.weight(1f)
                .padding(20.dp)
        ) {
            DragAndDropFile(text = "After") { file ->
                try {
                    json.decodeFromString<BenchmarkReport>(file.readText()).toUi()
                        .let { onUploadedAfter(it) }
                    afterError = ""
                } catch (e: Exception) {
                    afterError = "Error parsing `After` file: ${e.message}"
                }
            }
            Text(afterError, color = Color.Red)
        }
    }
}
