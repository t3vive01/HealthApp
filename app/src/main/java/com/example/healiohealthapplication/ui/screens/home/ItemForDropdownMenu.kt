package com.example.healthapplication.ui.screens.home

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ItemForDropdownMenu(
    textResId: Int, // string resource id which should be used in the dropdown item
    onClick: () -> Unit,
    fontWeight: FontWeight = FontWeight.Normal
) {
    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(textResId),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = fontWeight
            )
        },
        onClick = { onClick() }
    )
}