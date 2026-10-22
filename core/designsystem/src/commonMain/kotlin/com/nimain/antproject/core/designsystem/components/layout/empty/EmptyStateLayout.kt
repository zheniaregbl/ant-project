package com.nimain.antproject.core.designsystem.components.layout.empty

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ant_project.core.designsystem.generated.resources.Res
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.nimain.antproject.core.designsystem.components.layout.StateLayout

@Composable
fun EmptyStateLayout(
    illustration: EmptyIllustration,
    text: String,
    modifier: Modifier = Modifier,
) {
    StateLayout(
        modifier = modifier,
        visual = {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(Res.getUri(illustration.path()))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                contentDescription = null,
                modifier = Modifier.width(260.dp),
                alpha = 1f,
            )
        },
        text = text,
    )
}

private fun EmptyIllustration.path(): String =
    when (this) {
        EmptyIllustration.Inbox -> "files/illustrations/empty_inbox_svg.svg"
        EmptyIllustration.Today -> "files/illustrations/empty_today_svg.svg"
        EmptyIllustration.Tags -> "files/illustrations/empty_tags_svg.svg"
        EmptyIllustration.Project -> "files/illustrations/empty_project_svg.svg"
    }
