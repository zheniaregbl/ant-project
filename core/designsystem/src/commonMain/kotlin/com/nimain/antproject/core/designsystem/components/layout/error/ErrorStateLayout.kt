package com.nimain.antproject.core.designsystem.components.layout.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ant_project.core.designsystem.generated.resources.Res
import ant_project.core.designsystem.generated.resources.local_storage_error_svg
import com.nimain.antproject.core.designsystem.components.button.AntButton
import com.nimain.antproject.core.designsystem.components.button.AntButtonVariant
import com.nimain.antproject.core.designsystem.components.layout.StateLayout
import org.jetbrains.compose.resources.painterResource

@Composable
fun ErrorStateLayout(
    title: String,
    text: String,
    onClickRepeat: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StateLayout(
        modifier = modifier,
        visual = {
            Box(
                modifier =
                    Modifier
                        .size(64.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error.copy(alpha = .12f),
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.local_storage_error_svg),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        action = {
            AntButton(
                text = "Попробовать снова",
                onClick = onClickRepeat,
                variant = AntButtonVariant.Inverse,
            )
        },
    )
}
