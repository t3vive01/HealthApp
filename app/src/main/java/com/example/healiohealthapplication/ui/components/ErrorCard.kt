package com.example.healthapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthapplication.R
import com.example.healthapplication.ui.theme.ErrorRedDarker
import com.example.healthapplication.ui.theme.ErrorRedLighter

@Composable
fun ErrorCard(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ErrorRedLighter),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.error_card_icon_description),
                tint = ErrorRedDarker,
                modifier = Modifier.align(Alignment.CenterEnd).padding(12.dp).clickable { onDismiss() }
            )
            Text(
                text = errorMessage,
                color = ErrorRedDarker,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 32.dp, vertical = 24.dp)
            )
        }
    }
}
