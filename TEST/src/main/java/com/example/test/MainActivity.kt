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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.ui.theme.AndTheme
import net.pettip.app.CompoChips

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
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content() {
    //val context = LocalContext.current
    val shape = RoundedCornerShape(20.0.dp)
    val width = 0.5.dp
    val padding = 12.0.dp
    val modifier = Modifier
        .fillMaxSize()
        .padding(20.0.dp)
        .clip(shape = shape)
        .border(
            width = width,
            color = MaterialTheme.colorScheme.outline,
            shape = shape,
        )
        .clickable {}
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    var containerColor by remember { mutableStateOf(primary) }
    var contentColor by remember { mutableStateOf(onPrimary) }
    CompoChips(modifier, padding, onColorClick = { color, text ->
        //Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        containerColor = color
        contentColor = text
    }) {}
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.0.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        FloatingActionButton(
            onClick = {},
            containerColor = containerColor,
            contentColor = contentColor,
        ) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppContent() {
    AndTheme {
        Content()
    }
}