package com.nimain.antproject.tasks.components.quickadd

data class QuickAddUiState(
    val text: String = "",
    val status: SubmitStatus = SubmitStatus.Idle,
)
