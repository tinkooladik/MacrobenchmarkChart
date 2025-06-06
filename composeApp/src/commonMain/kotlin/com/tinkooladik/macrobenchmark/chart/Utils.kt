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
import java.util.concurrent.TimeUnit

val json = Json {
    ignoreUnknownKeys = true
}

/** Captures a composable and renders it into an ImageBitmap */
fun captureComposableAsImage(
    width: Int,
    height: Int,
    density: Float,
    folder: String = "reports",
    title: String = "report",
    copyToClipboard: Boolean = true,
    content: @Composable () -> Unit
): Boolean {
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
    if (copyToClipboard) {
        return copyFileToClipboardMacOS(out)
    }
    return data != null
}

fun copyFileToClipboardMacOS(file: File): Boolean {
    if (!file.exists()) {
        println("❌ File not found: ${file.absolutePath}")
        return false
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
        return true
    } catch (e: Exception) {
        println("❌ Failed to copy file to clipboard: ${e.message}")
        return false
    }
}

fun selectFolder(): File? {
    return try {
        val script = """
            set folderPath to POSIX path of (choose folder with prompt "Select Folder")
            return folderPath
        """.trimIndent()

        val process = ProcessBuilder("osascript", "-e", script)
            .redirectErrorStream(true)
            .start()

        val result = process.inputStream.bufferedReader().readLines()
        process.waitFor(2, TimeUnit.SECONDS)

        // Extract last line (which contains the actual folder path)
        val folderPath = result.lastOrNull()?.trim()

        if (!folderPath.isNullOrBlank() && File(folderPath).exists()) {
            println("Selected folder: $folderPath")
            File(folderPath)
        } else {
            println("❌ Folder selection failed, result is blank")
            null
        }
    } catch (e: Exception) {
        println("❌ Folder selection failed: ${e.message}")
        null
    }
}
