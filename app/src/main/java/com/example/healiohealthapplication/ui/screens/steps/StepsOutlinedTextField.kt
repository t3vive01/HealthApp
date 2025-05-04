package com.example.healthapplication.ui.screens.steps

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun StepsOutlinedTextField(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { newValue ->
            val parsedValue = newValue.toIntOrNull()
            onValueChange(parsedValue)
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    )
}