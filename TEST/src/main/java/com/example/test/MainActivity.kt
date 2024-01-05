/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.22
 *
 */

package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.ui.theme.AndTheme

/**
 * @Project     : PetTip-Android
 * @FileName    : MainActivity
 * @Date        : 2023-12-22
 * @author      : isyuun
 * @description : com.example.test
 * @see com.example.test.MainActivity
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndTheme {
                Surface {
                    AppContent()
                    Column/*(modifier = Modifier.fillMaxSize())*/ {
                        FloatingActionButton(onClick = {}) {
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    var enabled by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorChips()

        var text = if (enabled) "Btn:primary:enable" else "Btn:inversePrimary:disable"
        var color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                enabled = !enabled
            },
            colors = ButtonDefaults.buttonColors(color),
        ) { Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis) }

        text = "primary"
        color = MaterialTheme.colorScheme.primary
        ControllerWithColorSchemeLabel(
            controller = {
                Button(onClick = {}, enabled = enabled, colors = ButtonDefaults.buttonColors()) {
                    Text(
                        text = text,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                }
            },
            text = text,
            color = color,
        )

        text = "primaryContainer"
        color = MaterialTheme.colorScheme.primaryContainer
        ControllerWithColorSchemeLabel(
            controller = {
                Button(onClick = {}, enabled = enabled, colors = ButtonDefaults.buttonColors(color)) {
                    Text(
                        text = text,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                }
            },
            text = text,
            color = color,
        )

        text = "inversePrimary"
        color = MaterialTheme.colorScheme.inversePrimary
        ControllerWithColorSchemeLabel(
            controller = {
                Button(onClick = {}, enabled = enabled, colors = ButtonDefaults.buttonColors(color)) {
                    Text(
                        text = text,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                }
            },
            text = text,
            color = color,
        )

        text = "secondary"
        color = MaterialTheme.colorScheme.secondary
        ControllerWithColorSchemeLabel(
            controller = {
                Button(onClick = {}, enabled = enabled, colors = ButtonDefaults.buttonColors(color)) {
                    Text(
                        text = text,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                }
            },
            text = text,
            color = color,
        )

        text = "tertiary"
        color = MaterialTheme.colorScheme.tertiary
        ControllerWithColorSchemeLabel(
            controller = {
                Button(onClick = {}, enabled = enabled, colors = ButtonDefaults.buttonColors(color)) {
                    Text(
                        text = text,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                }
            },
            text = text,
            color = color,
        )

        text = "error"
        color = MaterialTheme.colorScheme.error
        ControllerWithColorSchemeLabel(
            controller = {
                Button(onClick = {}, enabled = enabled, colors = ButtonDefaults.buttonColors(color)) {
                    Text(
                        text = text,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                }
            },
            text = text,
            color = color,
        )

        text = "TabRow"
        color = MaterialTheme.colorScheme.primary
        ControllerWithColorSchemeLabel(
            controller = {
                TabRow(selectedTabIndex = 1) {
                    Tab(selected = true, onClick = { }, enabled = enabled) {
                        Icon(imageVector = Icons.Default.Face, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Tab",
                            maxLines = 1,
                        )
                    }
                    Tab(selected = true, onClick = { }, enabled = enabled) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Tab",
                            maxLines = 1,
                        )
                    }
                    Tab(selected = true, onClick = { }, enabled = enabled) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Tab",
                            maxLines = 1,
                        )
                    }
                }
            },
            text = text,
            color = color,
        )

        text = "Card"
        color = MaterialTheme.colorScheme.surface
        ControllerWithColorSchemeLabel(
            controller = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = text,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            },
            text = text,
            color = MaterialTheme.colorScheme.surface,
        )

        text = "TextField"
        color = MaterialTheme.colorScheme.surface
        ControllerWithColorSchemeLabel(
            controller = {
                var value1 by remember { mutableStateOf(TextFieldValue()) }
                TextField(
                    value = value1,
                    onValueChange = { value1 = it },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = text,
            color = color,
        )

        text = "TextField with Outline"
        color = MaterialTheme.colorScheme.surface
        ControllerWithColorSchemeLabel(
            controller = {
                var value2 by remember { mutableStateOf(TextFieldValue()) }
                OutlinedTextField(
                    value = value2,
                    onValueChange = { value2 = it },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = text,
            color = color,
        )
    }
}

fun colorToHexString(color: Color): String {
    // Color을 ARGB 형식의 정수로 변환
    val argb = android.graphics.Color.argb(
        (color.alpha * 255).toInt(),
        (color.red * 255).toInt(),
        (color.green * 255).toInt(),
        (color.blue * 255).toInt()
    )

    // 정수를 16진수 문자열로 변환
    val hexString = Integer.toHexString(argb)

    // "0x"를 앞에 붙여서 반환
    return "0x$hexString".uppercase()
}

@Composable
fun ColorChip(label: String, color: Color, text: Color = MaterialTheme.colorScheme.onPrimary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(8.dp)
    ) {
        Text(
            text = label,
            color = text,
            textAlign = TextAlign.Start,
            style = LocalTextStyle.current,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(4.dp)
        )
        Text(
            text = "(${colorToHexString(color)})",
            color = text,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .padding(4.dp)
        )
    }
}

@Composable
fun ColorChips() {
    ColorChip(label = "primary", color = MaterialTheme.colorScheme.primary, text = MaterialTheme.colorScheme.onPrimary)
    ColorChip(label = "onPrimary", color = MaterialTheme.colorScheme.onPrimary, text = MaterialTheme.colorScheme.primary)
    ColorChip(label = "primaryContainer", color = MaterialTheme.colorScheme.primaryContainer, text = MaterialTheme.colorScheme.onPrimaryContainer)
    ColorChip(label = "onPrimaryContainer", color = MaterialTheme.colorScheme.onPrimaryContainer, text = MaterialTheme.colorScheme.primaryContainer)
    ColorChip(label = "inversePrimary", color = MaterialTheme.colorScheme.inversePrimary, text = MaterialTheme.colorScheme.primary)
    ColorChip(label = "secondary", color = MaterialTheme.colorScheme.secondary, text = MaterialTheme.colorScheme.onSecondary)
    ColorChip(label = "onSecondary", color = MaterialTheme.colorScheme.onSecondary, text = MaterialTheme.colorScheme.secondary)
    ColorChip(label = "secondaryContainer", color = MaterialTheme.colorScheme.secondaryContainer, text = MaterialTheme.colorScheme.onSecondaryContainer)
    ColorChip(label = "onSecondaryContainer", color = MaterialTheme.colorScheme.onSecondaryContainer, text = MaterialTheme.colorScheme.secondaryContainer)
    ColorChip(label = "tertiary", color = MaterialTheme.colorScheme.tertiary, text = MaterialTheme.colorScheme.onTertiary)
    ColorChip(label = "onTertiary", color = MaterialTheme.colorScheme.onTertiary, text = MaterialTheme.colorScheme.tertiary)
    ColorChip(label = "tertiaryContainer", color = MaterialTheme.colorScheme.tertiaryContainer, text = MaterialTheme.colorScheme.onTertiaryContainer)
    ColorChip(label = "onTertiaryContainer", color = MaterialTheme.colorScheme.onTertiaryContainer, text = MaterialTheme.colorScheme.tertiaryContainer)
    ColorChip(label = "background", color = MaterialTheme.colorScheme.background, text = MaterialTheme.colorScheme.onBackground)
    ColorChip(label = "onBackground", color = MaterialTheme.colorScheme.onBackground, text = MaterialTheme.colorScheme.background)
    ColorChip(label = "surface", color = MaterialTheme.colorScheme.surface, text = MaterialTheme.colorScheme.onSurface)
    ColorChip(label = "onSurface", color = MaterialTheme.colorScheme.onSurface, text = MaterialTheme.colorScheme.surface)
    ColorChip(label = "surfaceVariant", color = MaterialTheme.colorScheme.surfaceVariant, text = MaterialTheme.colorScheme.onSurfaceVariant)
    ColorChip(label = "onSurfaceVariant", color = MaterialTheme.colorScheme.onSurfaceVariant, text = MaterialTheme.colorScheme.surfaceVariant)
    ColorChip(label = "surfaceTint", color = MaterialTheme.colorScheme.surfaceTint)
    ColorChip(label = "inverseSurface", color = MaterialTheme.colorScheme.inverseSurface, text = MaterialTheme.colorScheme.inverseOnSurface)
    ColorChip(label = "inverseOnSurface", color = MaterialTheme.colorScheme.inverseOnSurface, text = MaterialTheme.colorScheme.inverseSurface)
    ColorChip(label = "error", color = MaterialTheme.colorScheme.error, text = MaterialTheme.colorScheme.onError)
    ColorChip(label = "onError", color = MaterialTheme.colorScheme.onError, text = MaterialTheme.colorScheme.error)
    ColorChip(label = "errorContainer", color = MaterialTheme.colorScheme.errorContainer, text = MaterialTheme.colorScheme.onErrorContainer)
    ColorChip(label = "onErrorContainer", color = MaterialTheme.colorScheme.onErrorContainer, text = MaterialTheme.colorScheme.errorContainer)
    ColorChip(label = "outline", color = MaterialTheme.colorScheme.outline, text = MaterialTheme.colorScheme.outlineVariant)
    ColorChip(label = "outlineVariant", color = MaterialTheme.colorScheme.outlineVariant, text = MaterialTheme.colorScheme.outline)
    ColorChip(label = "scrim", color = MaterialTheme.colorScheme.onSurface)
}

@Composable
fun ControllerWithColorSchemeLabel(
    controller: @Composable () -> Unit,
    text: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 컨트롤러 위에 컬러 스킴 값 표시
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            //modifier = Modifier.padding(8.dp)
        )
        // 실제 컨트롤러
        controller()
        // 컬러 값확인
        Text(
            text = colorToHexString(color),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppContent() {
    AndTheme {
        // 앱 UI 구성
        AppContent()
    }
}