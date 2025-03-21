package com.tinkooladik.macrobenchmark.chart

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macrobenchmark.chart.views.MainContent
import com.tinkooladik.macrobenchmark.chart.views.UploadFiles
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scrollState = rememberScrollState()
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
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
                    MainContent(
                        before = before!!,
                        after = after!!,
                        showSnackbar = { text ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(text)
                            }
                        })
                }
            }

            // Add a vertical scrollbar
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState),
                modifier = Modifier.fillMaxHeight().padding(end = 8.dp)
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .wrapContentWidth()
            ) { snackbarData ->
                Snackbar(
                    modifier = Modifier
                        .widthIn(min = 150.dp, max = 400.dp),
                    content = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center // ✅ Centers text
                        ) {
                            Text(snackbarData.message)
                        }
                    }
                )
            }
        }
    }
}

