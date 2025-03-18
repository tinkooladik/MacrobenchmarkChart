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

/** Captures a composable and renders it into an ImageBitmap */
fun captureComposableAsImage(
    width: Int,
    height: Int,
    density: Float,
    // todo let user chose folder
    folder: String = "reports",
    title: String = "report",
    content: @Composable () -> Unit
) {
    val scene = ImageComposeScene(
        width = width,
        height = height,
        density = Density(density),
        coroutineContext = Dispatchers.Unconfined
    ) {
        content()
    }

    val img = scene.render()

    val bitmap = Bitmap.makeFromImage(img)
    val data = Image.makeFromBitmap(bitmap).use { image ->
        image.encodeToData(EncodedImageFormat.PNG)
    }
    val reportsDir = File(folder)
    if (!reportsDir.exists()) {
        reportsDir.mkdirs()
    }
    val out = File(reportsDir, "$title.png")
    out.delete()
    data?.bytes?.let {
        out.writeBytes(data.bytes)
    }
    copyFileToClipboardMacOS(out)
}

fun copyFileToClipboardMacOS(file: File) {
    if (!file.exists()) {
        println("❌ File not found: ${file.absolutePath}")
        return
    }

    try {
        val appleScript = """
            set theFile to POSIX file "${file.absolutePath}"
            set theList to {theFile}
            set theClipboard to theList as «class furl»
            set the clipboard to theClipboard
        """.trimIndent()

        val process = ProcessBuilder("osascript", "-e", appleScript)
            .redirectErrorStream(true)
            .start()

        process.waitFor()

        println("✅ File copied to clipboard (macOS): ${file.absolutePath}")
    } catch (e: Exception) {
        println("❌ Failed to copy file to clipboard: ${e.message}")
    }
}
