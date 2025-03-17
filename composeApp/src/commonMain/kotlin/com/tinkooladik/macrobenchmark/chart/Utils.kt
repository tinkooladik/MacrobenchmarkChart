package com.tinkooladik.macrobenchmark.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File

val json = Json {
    ignoreUnknownKeys = true
}

val benchmarkComparisonRules: Map<String, String> = mapOf(
    "frameCount" to "The higher, the better. More frames indicate smoother performance.",
    "memoryHeapSizeLastKb" to "The lower, the better. Less heap memory usage reduces memory pressure.",
    "memoryHeapSizeMaxKb" to "The lower, the better. Indicates peak memory usage; high values suggest memory inefficiencies.",
    "memoryRssAnonLastKb" to "The lower, the better. Represents the actual RAM used; high values indicate more memory consumption.",
    "memoryRssAnonMaxKb" to "The lower, the better. High peak values may lead to performance degradation due to memory pressure.",
    "memoryRssFileLastKb" to "The lower, the better. Measures memory-mapped file usage; reducing this can improve efficiency.",
    "memoryRssFileMaxKb" to "The lower, the better. High values mean more file-backed memory in RAM, which might indicate excessive caching.",
    "frameDurationCpuMs" to "The lower, the better. Measures frame rendering time; lower values mean smoother performance.",
    "frameOverrunMs" to "The lower, the better. Measures frame rendering delays; high values indicate lag and jank."
)

/** Captures a composable and renders it into an ImageBitmap */
fun captureComposableAsImage(
    width: Int,
    height: Int,
    content: @Composable () -> Unit
) {
    val scene = ImageComposeScene(
        width = width,
        height = height,
        density = Density(3f),
        coroutineContext = Dispatchers.Unconfined
    ) {
        content()
    }
    val img = scene.render()

    val bitmap = Bitmap.makeFromImage(img)
    val data = Image.makeFromBitmap(bitmap).use { image ->
        image.encodeToData(EncodedImageFormat.PNG)
    }
    val out = File("test.png")
    out.delete() // If you want to overwrite like that
    data?.bytes?.let {
        out.writeBytes(data.bytes)
    }
}
