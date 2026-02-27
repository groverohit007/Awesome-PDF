package com.awesomepdf.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.awesomepdf.domain.usecase.MergePdfUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MergePdfWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val mergePdfUseCase: MergePdfUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val uris = inputData.getStringArray(KEY_INPUT_URIS)?.toList().orEmpty()
        val outputName = inputData.getString(KEY_OUTPUT_NAME) ?: "merged_${System.currentTimeMillis()}.pdf"

        if (uris.size < 2) {
            return Result.failure(workDataOf(KEY_ERROR to "Select at least 2 PDFs"))
        }

        return try {
            setProgress(workDataOf(KEY_PROGRESS to 5))
            val outputPath = mergePdfUseCase(uris, outputName) { progress ->
                setProgressAsync(workDataOf(KEY_PROGRESS to progress))
            }
            Result.success(workDataOf(KEY_OUTPUT_PATH to outputPath, KEY_PROGRESS to 100))
        } catch (t: Throwable) {
            Result.failure(workDataOf(KEY_ERROR to (t.message ?: "Merge failed")))
        }
    }

    companion object {
        const val KEY_INPUT_URIS = "key_input_uris"
        const val KEY_OUTPUT_NAME = "key_output_name"
        const val KEY_OUTPUT_PATH = "key_output_path"
        const val KEY_PROGRESS = "key_progress"
        const val KEY_ERROR = "key_error"

        fun buildInputData(uris: List<String>, outputName: String): Data {
            return workDataOf(
                KEY_INPUT_URIS to uris.toTypedArray(),
                KEY_OUTPUT_NAME to outputName
            )
        }
    }
}
