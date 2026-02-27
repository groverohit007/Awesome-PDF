package com.awesomepdf.presentation.main.tools

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.awesomepdf.worker.MergePdfWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ToolsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    private val _selectedUris = MutableStateFlow<List<String>>(emptyList())
    val selectedUris = _selectedUris.asStateFlow()

    private val _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()

    private val _isMerging = MutableStateFlow(false)
    val isMerging = _isMerging.asStateFlow()

    private val _resultPath = MutableStateFlow<String?>(null)
    val resultPath = _resultPath.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _activeWorkId = MutableStateFlow<UUID?>(null)

    fun onPdfSelection(uris: List<String>) {
        _selectedUris.value = uris
        _error.value = null
    }

    fun startMerge(outputName: String = "merged_${System.currentTimeMillis()}.pdf") {
        if (_selectedUris.value.size < 2) {
            _error.value = "Please select at least 2 PDFs"
            return
        }

        _isMerging.value = true
        _progress.value = 0
        _error.value = null
        _resultPath.value = null

        val request = OneTimeWorkRequestBuilder<MergePdfWorker>()
            .setInputData(MergePdfWorker.buildInputData(_selectedUris.value, outputName))
            .build()

        _activeWorkId.value = request.id
        workManager.enqueue(request)

        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(request.id).collect { info ->
                handleWorkInfo(info)
            }
        }
    }

    private fun handleWorkInfo(info: WorkInfo) {
        _progress.value = info.progress.getInt(MergePdfWorker.KEY_PROGRESS, _progress.value)
        when (info.state) {
            WorkInfo.State.SUCCEEDED -> {
                _isMerging.value = false
                _progress.value = 100
                _resultPath.value = info.outputData.getString(MergePdfWorker.KEY_OUTPUT_PATH)
            }
            WorkInfo.State.FAILED -> {
                _isMerging.value = false
                _error.value = info.outputData.getString(MergePdfWorker.KEY_ERROR) ?: "Merge failed"
            }
            WorkInfo.State.CANCELLED -> {
                _isMerging.value = false
                _error.value = "Merge cancelled"
            }
            else -> Unit
        }
    }
}
