package com.nimain.antproject.core.designsystem.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
internal fun antTypography() =
    Typography(
        displayMedium =
            BaseStyle.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 44.sp,
                lineHeight = 44.sp,
                letterSpacing = (-0.03).em,
            ),
        displaySmall =
            BaseStyle.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 42.sp,
                letterSpacing = (-0.03).em,
            ),
        headlineMedium =
            BaseStyle.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                lineHeight = 31.sp,
                letterSpacing = (-0.02).em,
            ),
        titleLarge =
            BaseStyle.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 26.sp,
                letterSpacing = (-0.02).em,
            ),
        bodyLarge =
            BaseStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 21.sp,
            ),
        bodyMedium =
            BaseStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 22.sp,
            ),
        labelLarge =
            BaseStyle.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 14.sp,
            ),
    )
