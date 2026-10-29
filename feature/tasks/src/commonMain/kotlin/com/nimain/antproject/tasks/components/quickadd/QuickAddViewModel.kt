package com.nimain.antproject.tasks.components.quickadd

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskRepository
import com.nimain.antproject.core.domain.tasks.result.CreateTaskResult
import kotlinx.coroutines.launch

class QuickAddViewModel(
    private val repository: TaskRepository,
) : ViewModel() {
    val title = TextFieldState()

    var status: SubmitStatus by mutableStateOf(SubmitStatus.Idle)
        private set

    val canSubmit: Boolean
        get() =
            title.text.isNotBlank() &&
                (status == SubmitStatus.Idle || status == SubmitStatus.Failed)

    fun onSubmit() {
        if (!canSubmit) return
        val text = title.text.toString().trim()
        status = SubmitStatus.Saving

        viewModelScope.launch {
            status =
                when (
                    val result =
                        repository.createTask(
                            draft = TaskDraft(title = text),
                            projectId = null,
                        )
                ) {
                    is CreateTaskResult.Success -> SubmitStatus.Created(result.taskId)
                    CreateTaskResult.Failure -> SubmitStatus.Failed
                }
        }
    }

    fun onCreatedHandled() {
        if (status is SubmitStatus.Created) {
            title.clearText()
            status = SubmitStatus.Idle
        }
    }
}
