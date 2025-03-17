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
import androidx.compose.ui.unit.sp
import com.tinkooladik.macrobenchmark.chart.json
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReport
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macrobenchmark.chart.models.toUi

@Composable
fun UploadFiles(
    onUploadedBefore: (BenchmarkReportUi) -> Unit,
    onUploadedAfter: (BenchmarkReportUi) -> Unit
) {
    var beforeReport by remember { mutableStateOf<BenchmarkReportUi?>(null) }
    var afterReport by remember { mutableStateOf<BenchmarkReportUi?>(null) }
    var beforeError by remember { mutableStateOf<String?>(null) }
    var afterError by remember { mutableStateOf<String?>(null) }

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(
            modifier = Modifier.weight(1f)
                .padding(20.dp)
        ) {
            DragAndDropFile(text = "Before") { file ->
                try {
                    json.decodeFromString<BenchmarkReport>(file.readText()).toUi()
                        .let {
                            beforeReport = it
                            onUploadedBefore(it)
                        }
                    beforeError = ""
                } catch (e: Exception) {
                    beforeError = "Error parsing `Before` file: ${e.message}"
                    beforeReport = null
                }
            }
            beforeError?.let {
                Text(it, color = Color.Red)
            }
            beforeReport?.let {
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Text("Device: ${it.device}", fontSize = 12.sp)
                    Text("Iterations: ${it.iterations}", fontSize = 12.sp)
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f)
                .padding(20.dp)
        ) {
            DragAndDropFile(text = "After") { file ->
                try {
                    json.decodeFromString<BenchmarkReport>(file.readText()).toUi()
                        .let {
                            afterReport = it
                            onUploadedAfter(it)
                        }
                    afterError = ""
                } catch (e: Exception) {
                    afterError = "Error parsing `After` file: ${e.message}"
                    afterReport = null
                }
            }
            afterError?.let {
                Text(it, color = Color.Red)
            }
            afterReport?.let {
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Text("Device: ${it.device}", fontSize = 12.sp)
                    Text("Iterations: ${it.iterations}", fontSize = 12.sp)
                }
            }
        }
    }

    if (beforeReport != null && afterReport != null &&
        beforeReport!!.isTheSame(afterReport!!).not()
    ) {
        Text("Before / After data do not match.", color = Color.Red)
    }
}
