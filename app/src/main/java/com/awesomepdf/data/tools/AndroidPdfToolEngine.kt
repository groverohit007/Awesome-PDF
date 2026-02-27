package com.awesomepdf.data.tools

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.awesomepdf.domain.tools.PdfToolEngine
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPdfToolEngine @Inject constructor(
    @ApplicationContext private val context: Context
) : PdfToolEngine {

    override suspend fun mergePdf(
        inputUris: List<String>,
        outputFileName: String,
        onProgress: (Int) -> Unit
    ): String {
        val outputDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "merged"
        )
        if (!outputDir.exists()) outputDir.mkdirs()

        val safeName = if (outputFileName.endsWith(".pdf", ignoreCase = true)) outputFileName else "$outputFileName.pdf"
        val outputFile = File(outputDir, safeName)

        val merger = PDFMergerUtility()
        inputUris.forEachIndexed { index, rawUri ->
            val stream = context.contentResolver.openInputStream(Uri.parse(rawUri))
                ?: throw IllegalStateException("Unable to open input PDF: $rawUri")
            merger.addSource(stream)
            val base = ((index + 1) * 80) / inputUris.size
            onProgress(base.coerceIn(1, 80))
        }

        FileOutputStream(outputFile).use { output ->
            merger.destinationStream = output
            merger.mergeDocuments(null)
        }
        onProgress(100)

        return outputFile.absolutePath
    }
}
