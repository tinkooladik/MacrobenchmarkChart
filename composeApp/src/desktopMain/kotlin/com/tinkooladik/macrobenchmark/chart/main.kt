package com.tinkooladik.macrobenchmark.chart

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

// ./gradlew run desktopRun -DmainClass=MainKt --quiet
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MacrobenchmarkChart",
    ) {
        App()
    }
}