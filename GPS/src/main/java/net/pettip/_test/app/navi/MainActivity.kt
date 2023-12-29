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

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import net.pettip.app.withClick
import net.pettip.gps.R
import net.pettip.ui.theme.APPTheme
import net.pettip.util.Log
import net.pettip.util.getMethodName

open class MainActivity : ComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private fun openMap(context: Context) {
        val intent = Intent(context, MapActivity::class.java)
        startActivity(intent)
    }

    private fun openGpx(context: Context) {
        val intent = Intent(context, GpxActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onResume()
        //if (!application.start) {
        //    openMap(this)
        //}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate(savedInstanceState)
        setContent()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setContent() {
        setContent {
            SetContent()
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
    private fun SetContent() {
        APPTheme {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet { /* Drawer content */ }
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
                        var selectedTabIndex by remember { mutableStateOf(0) }
                        BottomAppBar {
                            TabRow(
                                selectedTabIndex = selectedTabIndex,
                                indicator = { tabPositions ->
                                    TabRowDefaults.Indicator(
                                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    )
                                },
                            ) {
                                Tab(
                                    selected = selectedTabIndex == 0,
                                    onClick = withClick {
                                        selectedTabIndex = 0
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Snackbar")
                                        }
                                    },
                                    //enabled = enabled,
                                ) {
                                    Icon(imageVector = Icons.Default.Face, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tab",
                                        maxLines = 1,
                                    )
                                }
                                Tab(
                                    selected = selectedTabIndex == 1,
                                    onClick = withClick {
                                        selectedTabIndex = 1
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Snackbar")
                                        }
                                    },
                                    //enabled = enabled,
                                ) {
                                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tab",
                                        maxLines = 1,
                                    )
                                }
                                Tab(
                                    selected = selectedTabIndex == 2,
                                    onClick = withClick {
                                        selectedTabIndex = 2
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Snackbar")
                                        }
                                    },
                                    //enabled = enabled,
                                ) {
                                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tab",
                                        maxLines = 1,
                                    )
                                }
                                Tab(
                                    selected = selectedTabIndex == 3,
                                    onClick = withClick {
                                        selectedTabIndex = 3
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Snackbar")
                                        }
                                    },
                                    //enabled = enabled,
                                ) {
                                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tab",
                                        maxLines = 1,
                                    )
                                }
                            }
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = withClick { showBottomSheet = true },
                            shape = CircleShape,
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,
                ) { innerPadding ->
                    ShowBottomSheet(showBottomSheet = showBottomSheet) { showBottomSheet = false }
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Content()
                    }
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
                    AppContent(modifier, padding)
                }
            }   //Box2
        }   //Surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.0.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.0.dp)
            ) {
                //var clicked by remember { mutableStateOf(false) }
                //ExtendedFloatingActionButton(
                //    text = { Text(text = stringResource(net.pettip.R.string.start), fontSize = 12.sp) },
                //    icon = { Icon(if (clicked) Icons.Default.Close else Icons.Default.Add, contentDescription = null) },
                //    onClick = withClick { clicked = !clicked },
                //    shape = CircleShape,
                //    expanded = clicked,
                //)
                FloatingActionButton(
                    onClick = withClick { openMap(context) },
                    shape = CircleShape,
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
                FloatingActionButton(
                    onClick = withClick { openMap(context) },
                    shape = CircleShape,
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
            }   //Column
        }   //Box
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppContent(modifier: Modifier, padding: Dp) {
        val context = LocalContext.current
        Column(
            modifier = modifier
                .padding(8.0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(padding),
        ) {
            Text(
                text = "Column",
                modifier = Modifier
                    .padding(horizontal = padding)
                    .clickable(onClick = withClick {}),
            )
            Divider()
            var enabled by remember { mutableStateOf(true) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { enabled = !enabled },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Enabled")
                Switch(
                    checked = enabled,
                    onCheckedChange = { enabled = it },
                    colors = SwitchDefaults.colors()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { enabled = !enabled },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Enabled")
                Switch(
                    checked = enabled,
                    onCheckedChange = { enabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { enabled = !enabled },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var checked by remember { mutableStateOf(true) }
                Text("Enabled")
                Switch(
                    checked = enabled,
                    onCheckedChange = { enabled = it },
                    thumbContent = if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { openMap(context) },
                //colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                enabled = enabled,
            ) { Text(text = "Btn:primary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { openGpx(context) },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary),
                enabled = enabled,
            ) { Text(text = "Btn:inversePrimary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                //colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.secondaryContainer),
                enabled = enabled,
            ) { Text(text = "FilledTonalButton:secondaryContainer", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                //colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.surface),
                enabled = enabled,
            ) { Text(text = "ElevatedButton:surface", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                //colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
                enabled = enabled,
            ) { Text(text = "OutlinedButton:Color.Transparent", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                //colors = ButtonDefaults.textButtonColors(Color.Transparent),
                enabled = enabled,
            ) { Text(text = "TextButton:Color.Transparent", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            IconButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                //colors = IconButtonDefaults.iconButtonColors(Color.Transparent),
                enabled = enabled,
            ) {
                Row(modifier = Modifier.clickable(onClick = withClick {})) {
                    Icon(Icons.Default.Add, contentDescription = "Add Icon")
                    Text(text = "IconButton:Color.Transparent", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            var value1 by remember { mutableStateOf(TextFieldValue("TextField:surfaceVariant")) }
            TextField(
                value = value1, onValueChange = { value1 = it },
                singleLine = true,
                enabled = enabled,
                isError = false,
            )
            var value2 by remember { mutableStateOf(TextFieldValue("TextField:surfaceVariant:Error")) }
            TextField(
                value = value2, onValueChange = { value2 = it },
                singleLine = true,
                enabled = enabled,
                isError = true,
            )
            var value3 by remember { mutableStateOf(TextFieldValue("OutlinedTextField")) }
            OutlinedTextField(
                value = value3, onValueChange = { value3 = it },
                singleLine = true,
                enabled = enabled,
                isError = false,
            )
            var value4 by remember { mutableStateOf(TextFieldValue("OutlinedTextField:Error")) }
            OutlinedTextField(
                value = value4, onValueChange = { value4 = it },
                singleLine = true,
                enabled = enabled,
                isError = true,
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = withClick {}),
            ) {
                Text(
                    text = "Card:surfaceVariant",
                    modifier = Modifier.padding(12.0.dp),
                )
            }
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = withClick {}),
            ) {
                Text(
                    text = "ElevatedCard:surface",
                    modifier = Modifier.padding(12.0.dp),
                )
            }
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = withClick {}),
            ) {
                Text(
                    text = "OutlinedCard:surface",
                    modifier = Modifier.padding(12.0.dp),
                )
            }
            AssistChip(
                onClick = withClick { },
                label = { Text("Assist chip") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                enabled = enabled,
            )
            var selected by remember { mutableStateOf(false) }
            FilterChip(
                selected = selected,
                onClick = withClick { selected = !selected },
                label = { Text("Filter chip") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Done",
                        Modifier.size(FilterChipDefaults.IconSize)
                    )
                },
                enabled = enabled,
            )
            InputChip(
                onClick = withClick { /*enabled = !enabled*/ },
                label = { Text("InputChip") },
                selected = enabled,
                avatar = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Localized description",
                        Modifier.size(InputChipDefaults.AvatarSize)
                    )
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Localized description",
                        Modifier.size(InputChipDefaults.AvatarSize)
                    )
                },
                enabled = enabled,
            )
            SuggestionChip(
                onClick = withClick { },
                label = { Text("Suggestion chip") },
                enabled = enabled,
            )
            var sliderPosition by remember { mutableFloatStateOf(0f) }
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                colors = SliderDefaults.colors(),
                steps = 3,
                valueRange = 0f..50f,
                enabled = enabled,
            )
            Text(text = sliderPosition.toString())
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 3,
                valueRange = 0f..50f,
                enabled = enabled,
            )
            Text(text = sliderPosition.toString())
            var rangePosition by remember { mutableStateOf(0f..100f) }
            RangeSlider(
                value = rangePosition,
                steps = 5,
                onValueChange = { range -> rangePosition = range },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    // launch some business logic update with the state you hold
                    // viewModel.updateSelectedSliderValue(sliderPosition)
                },
                enabled = enabled,
            )
            Text(text = sliderPosition.toString())
            Divider()
            Text(
                text = "Colors",
                modifier = Modifier
                    .padding(horizontal = padding)
                    .clickable(onClick = withClick {}),
            )
            ColorChips()
        } //Column
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
                .clickable(onClick = withClick {}),
        ) {
            Text(
                text = label,
                color = text,
                maxLines = 1,
                textAlign = TextAlign.Start,
                style = LocalTextStyle.current,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(4.dp),
            )
            Text(
                text = "(${colorToHexString(color)})",
                color = text,
                maxLines = 1,
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
        ColorChip(label = "scrim", color = MaterialTheme.colorScheme.scrim)
    }
}