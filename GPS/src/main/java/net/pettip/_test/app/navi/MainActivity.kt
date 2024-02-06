/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.20
 *
 */

package net.pettip._test.app.navi

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import net.pettip.app.CompoChips
import net.pettip.app.withClick
import net.pettip.gps.R
import net.pettip.gps.app.GPSApplication
import net.pettip.ui.theme.APPTheme
import net.pettip.util.Log
import net.pettip.util.getMethodName

open class MainActivity : ComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onResume() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent()
    }

    protected open fun setContent() {
        //Log.v(__CLASSNAME__, "${getMethodName()}...")
        setContent {
            APPTheme {
                SetContent()
            }
        }
    }

    @Preview(
        uiMode = UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark"
    )
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        name = "DefaultPreviewLight"
    )
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    protected open fun SetContent() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {}
            },
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            var showBottomSheet by remember { mutableStateOf(false) }
            Scaffold(
                modifier = Modifier
                    .clickable { },
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.app_name)) },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            //IconButton(onClick = { /* Handle search icon click */ }) {
                            //    Icon(Icons.Default.Search, contentDescription = "Search")
                            //}
                            //IconButton(onClick = { /* Handle settings icon click */ }) {
                            //    Icon(Icons.Default.Settings, contentDescription = "Settings")
                            //}
                            //IconButton(onClick = { /* Handle share icon click */ }) {
                            //    Icon(Icons.Default.Share, contentDescription = "Share")
                            //}
                            IconButton(onClick = { /* Handle share icon click */ }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "MoreVert")
                            }
                        },
                    )
                },
                bottomBar = {
                    BottomAppBar {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FloatingActionButton(
                                onClick = withClick {
                                    showBottomSheet = true
                                },
                                shape = CircleShape,
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                floatingActionButton = {
                    //FloatingActionButton(
                    //    onClick = withClick { showBottomSheet = true },
                    //    shape = CircleShape,
                    //) {
                    //    Icon(Icons.Default.Add, contentDescription = "Add")
                    //}
                },
                floatingActionButtonPosition = FabPosition.End,
            ) { innerPadding ->
                ShowBottomSheet(showBottomSheet = showBottomSheet) {
                    showBottomSheet = false
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Snackbar")
                    }
                }
                Box(modifier = Modifier.padding(innerPadding)) {
                    Content()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowBottomSheet(showBottomSheet: Boolean, onDismissRequest: () -> Unit) {
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                modifier = Modifier
                    .clickable { },
            ) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .clickable { },
                ) {
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismissRequest()
                            }
                        }
                    }) {
                        Text("Hide bottom sheet")
                    }
                }
            }
        }
    }

    @Composable
    fun Content() {
        val application = GPSApplication.instance
        val context = LocalContext.current
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
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = withClick(context) {}),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Text(text = "Blank:background", modifier = Modifier.padding(horizontal = padding), color = Color.Red)
            }
        }
        // A surface container using the 'surface'(default) color from the theme
        val primary = MaterialTheme.colorScheme.primary
        val onPrimary = MaterialTheme.colorScheme.onPrimary
        var containerColor by remember { mutableStateOf(primary) }
        var contentColor by remember { mutableStateOf(onPrimary) }
        Surface(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = withClick(context) {}),
            border = BorderStroke(0.9.dp, Color.Cyan),
        ) {
            Text(text = "Surface:surface", modifier = Modifier.padding(horizontal = padding))
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = modifier
                    .padding(2.0.dp)
                    .border(
                        width = width,
                        color = Color.Red,
                        shape = shape,
                    ),
            ) {
                Text(text = "Box1", modifier = Modifier.padding(horizontal = padding))
            } //Box1
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = modifier
                    .padding(4.0.dp)
                    .border(
                        width = width,
                        color = Color.Yellow,
                        shape = shape,
                    ),
            ) {
                Text(text = "Box2", modifier = Modifier.padding(horizontal = padding))
                //Row(
                //    modifier = modifier,
                //    //verticalAlignment = Alignment.CenterVertically,
                //) {
                //    Text(
                //        text = "Row", modifier = Modifier
                //            .padding(horizontal = padding)
                //            .weight(0.15f)
                //    )
                //    Box(
                //        modifier = modifier
                //            .weight(0.9f),
                //        ) {
                //        AppContent(modifier, padding)
                //    }
                //}   //Row
                Column(
                    modifier = Modifier
                        //.padding(4.0.dp)
                        .clickable { },
                ) {
                    CompoChips(modifier, padding, onColorClick = { color, text ->
                        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
                        containerColor = color
                        contentColor = text
                    }) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = withClick { application.openMap() },
                            //colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        ) { Text(text = "Btn:openMap:primary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = withClick { application.recent()?.let { application.openGpx(it) } },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary),
                        ) { Text(text = "Btn:openGpx:inversePrimary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    }
                }
            }   //Box2
        }   //Surface
        var open by remember { mutableStateOf(false) }
        val color = if (open) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.0.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(
                modifier = Modifier
                    //.padding(bottom = 100.0.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(12.0.dp),
            ) {
                net.pettip.R.string.start
                FloatingActionButton(
                    onClick = withClick {
                        containerColor = color
                        open = true
                        application.openMap()
                    },
                    shape = CircleShape,
                    containerColor = containerColor,
                    contentColor = contentColor
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.ArrowForward),
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(net.pettip.R.string.start),
                            fontSize = 12.sp,
                        )
                    }
                }
                net.pettip.R.string.stop
                FloatingActionButton(
                    onClick = withClick {
                        containerColor = color
                        open = false
                        application.recent()?.let { application.openGpx(it) }
                    },
                    shape = CircleShape,
                    containerColor = containerColor,
                    contentColor = contentColorFor(containerColor)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.Done),
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(net.pettip.R.string.stop),
                            fontSize = 12.sp,
                        )
                    }
                }
                //var clicked by remember { mutableStateOf(false) }
                //ExtendedFloatingActionButton(
                //    text = { Text(text = stringResource(net.pettip.R.string.start), fontSize = 12.sp) },
                //    icon = { Icon(if (clicked) Icons.Default.Close else Icons.Default.Add, contentDescription = null) },
                //    onClick = withClick { clicked = !clicked },
                //    shape = CircleShape,
                //    expanded = clicked,
                //)
            }   //Column
        }   //Box
    }
}