package com.nimain.antproject.tasks.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.components.bottomsheet.AntBottomSheet
import com.nimain.antproject.core.designsystem.components.bottomsheet.AntSheetItem
import com.nimain.antproject.core.designsystem.theme.AntTheme

@Composable
fun TaskActionsSheet(
    title: String,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    var deleteRequested by remember { mutableStateOf(false) }

    AntBottomSheet(
        onDismissRequest = {
            if (deleteRequested) onDelete()
            onDismiss()
        },
    ) { dismiss ->
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = AntTheme.spacing.screenHorizontal, vertical = 12.dp),
        )
        AntSheetItem(
            text = "Удалить",
            destructive = true,
            onClick = {
                if (!deleteRequested) {
                    deleteRequested = true
                    dismiss()
                }
            },
        )
    }
}
