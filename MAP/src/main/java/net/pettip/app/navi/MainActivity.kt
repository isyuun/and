/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.27
 *
 */

package net.pettip.app.navi

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import net.pettip.gps.R
import net.pettip.gps.app.GPSApplication
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : PetTip-Android
 * @FileName    : MainActivity
 * @Date        : 2023-12-27
 * @author      : isyuun
 * @description : net.pettip.app.navi
 * @see net.pettip.app.navi.MainActivity
 */
class MainActivity : net.pettip._test.app.navi.MainActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun setContent() {
        setContent {
            SetContent()
            Box {
                val application = GPSApplication.instance
                Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.recent()?.exists()}][${application.recent()}]")
                val context = LocalContext.current
                var showDialog by remember { mutableStateOf(false) }
                application.recent()?.let { recent -> showDialog = recent.exists() }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                            application.reset()
                        },
                        title = { Text(stringResource(id = R.string.walk_text_in_running)) },
                        text = { Text(stringResource(id = R.string.walk_text_in_restore)) },
                        confirmButton = {
                            // Confirm button
                            Button(
                                onClick = {
                                    // Handle confirm button click
                                    showDialog = false
                                    openMap(context)
                                }
                            ) {
                                Text(stringResource(id = android.R.string.ok))
                            }
                        },
                        dismissButton = {
                            // Dismiss button
                            Button(
                                onClick = {
                                    showDialog = false
                                    application.reset()
                                }
                            ) {
                                Text(stringResource(id = android.R.string.cancel))
                            }
                        }
                    )
                }//showDialog
            }
        }
    }
}