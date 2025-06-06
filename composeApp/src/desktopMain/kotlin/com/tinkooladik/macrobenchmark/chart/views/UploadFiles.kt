package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
    onUploadedBefore: (BenchmarkReportUi?) -> Unit,
    onUploadedAfter: (BenchmarkReportUi?) -> Unit
) {
    var beforeReport by remember { mutableStateOf<BenchmarkReportUi?>(null) }
    var afterReport by remember { mutableStateOf<BenchmarkReportUi?>(null) }

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        UploadFile(text = "Before") {
            onUploadedBefore(it)
            beforeReport = it
        }
        UploadFile(text = "After") {
            onUploadedAfter(it)
            afterReport = it
        }
    }

    if (beforeReport != null && afterReport != null &&
        beforeReport!!.isTheSame(afterReport!!).not()
    ) {
        Text("Before / After data do not match.", color = Color.Red)
    }
}

@Composable
private fun RowScope.UploadFile(
    text: String,
    onReport: (BenchmarkReportUi?) -> Unit
) {
    var report by remember { mutableStateOf<BenchmarkReportUi?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier.weight(1f)
            .padding(20.dp)
    ) {
        DragAndDropFile(text = text) { file ->
            try {
                json.decodeFromString<BenchmarkReport>(file.readText()).toUi()
                    .let {
                        onReport(it)
                        report = it
                    }
                error = null
            } catch (e: Exception) {
                error = "Error parsing `$text` file: ${e.message}"
                onReport(null)
                report = null
            }
        }
        error?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(start = 20.dp, top = 20.dp))
        }
        report?.let {
            Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
                Text("Device: ${it.device}", fontSize = 12.sp)
                Text("Iterations: ${it.iterations}", fontSize = 12.sp)
            }
        }
    }
}
