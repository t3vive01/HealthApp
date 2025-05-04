package com.example.healthapplication.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun ChangePageText(text: String, linkText: String, onClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append("$text ") // normal text

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color =  MaterialTheme.colorScheme.primary)) {
            addStringAnnotation(tag = "LINK", annotation = linkText, start = length, end = length + linkText.length) // marks the linkText with a LINK tag
            append(linkText) // adds the linkText into the original normal text string
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(fontSize = 16.sp, color = Color.Black),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "LINK", start = offset, end = offset) // checks if the click happened on the "LINK" tag
                .firstOrNull()?.let {
                    onClick() // if the text with the "LINK" tag was clicked, call onClick
                }
        }
    )
}