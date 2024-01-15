/*
 * Copyright (c) 2024. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2024.1.12
 *
 */

package net.pettip.util

import android.view.autofill.AutofillManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import kotlin.math.roundToInt

/**
 * @Project     : PetTip-Android
 * @FileName    : AutoFill
 * @Date        : 2024-01-12
 * @author      : isyuun
 * @description : net.pettip.util
 * @see <a href="https://medium.com/@bagadeshrp/compose-ui-textfield-autofill-6e2ac434e380">Compose UI TextField AutoFill</a>
 * @see <a href="https://bryanherbst.com/2021/04/13/compose-autofill/">Autofill with Jetpack Compose</a>
 * @see net.pettip.util.AutoFill
 */
fun Modifier.connectNode(handler: AutoFillHandler): Modifier {
    return with(handler) { fillBounds() }
}

fun Modifier.defaultFocusChangeAutoFill(handler: AutoFillHandler): Modifier {
    return this.then(
        Modifier.onFocusChanged {
            if (it.isFocused) {
                handler.request()
            } else {
                handler.cancel()
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AutoFillRequestHandler(
    autofillTypes: List<AutofillType> = listOf(),
    onFill: (String) -> Unit,
): AutoFillHandler {
    val view = LocalView.current
    val context = LocalContext.current
    var isFillRecently = remember { false }
    val autoFillNode = remember {
        AutofillNode(
            autofillTypes = autofillTypes,
            onFill = {
                isFillRecently = true
                onFill(it)
            }
        )
    }
    val autofill = LocalAutofill.current
    LocalAutofillTree.current += autoFillNode
    return remember {
        object : AutoFillHandler {
            val autofillManager = context.getSystemService(AutofillManager::class.java)
            override fun requestManual() {
                autofillManager.requestAutofill(
                    view,
                    autoFillNode.id,
                    autoFillNode.boundingBox?.toAndroidRect() ?: error("BoundingBox is not provided yet")
                )
            }

            override fun requestVerifyManual() {
                if (isFillRecently) {
                    isFillRecently = false
                    requestManual()
                }
            }

            override val autoFill: Autofill?
                get() = autofill

            override val autoFillNode: AutofillNode
                get() = autoFillNode

            override fun request() {
                autofill?.requestAutofillForNode(autofillNode = autoFillNode)
            }

            override fun cancel() {
                autofill?.cancelAutofillForNode(autofillNode = autoFillNode)
            }

            override fun Modifier.fillBounds(): Modifier {
                return this.then(
                    Modifier.onGloballyPositioned {
                        autoFillNode.boundingBox = it.boundsInWindow()
                    })
            }
        }
    }
}

fun Rect.toAndroidRect(): android.graphics.Rect {
    return android.graphics.Rect(
        left.roundToInt(),
        top.roundToInt(),
        right.roundToInt(),
        bottom.roundToInt()
    )
}

@OptIn(ExperimentalComposeUiApi::class)
interface AutoFillHandler {
    val autoFill: Autofill?
    val autoFillNode: AutofillNode
    fun requestVerifyManual()
    fun requestManual()
    fun request()
    fun cancel()
    fun Modifier.fillBounds(): Modifier
}

/**
 * @see <a href="https://medium.com/@bagadeshrp/compose-ui-textfield-autofill-6e2ac434e380">Compose UI TextField AutoFill</a>
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailField() {
    var email by remember { mutableStateOf("") }
    Column {
        val autoFillHandler = AutoFillRequestHandler(autofillTypes = listOf(AutofillType.EmailAddress),
            onFill = {
                email = it
            }
        )
        TextField(
            value = email,
            onValueChange = {
                email = it
                if (it.isEmpty()) autoFillHandler.requestVerifyManual()
            },
            placeholder = {
                Text(text = "Email")
            },
            modifier = Modifier
                .fillMaxWidth()
                .connectNode(handler = autoFillHandler)
                .defaultFocusChangeAutoFill(handler = autoFillHandler)
        )
        val focusManager = LocalFocusManager.current
        Button(onClick = { autoFillHandler.requestManual() }) {
            Text(text = "Show Auto Fill")
        }
        Button(onClick = { focusManager.clearFocus() }) {
            Text(text = "Clear Focus")
        }
    }

}

/**
 * @see <a href="https://bryanherbst.com/2021/04/13/compose-autofill/">Autofill with Jetpack Compose</a>
 */
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this
        .onGloballyPositioned {
            autofillNode.boundingBox = it.boundsInWindow()
        }
        .onFocusChanged { focusState ->
            autofill?.run {
                if (focusState.isFocused) {
                    requestAutofillForNode(autofillNode)
                } else {
                    cancelAutofillForNode(autofillNode)
                }
            }
        }
}