package net.pettip.gps.app

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import net.pettip._app.application
import net.pettip.gps.R
import net.pettip.singleton.G
import net.pettip.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

/**
 * @Project     : PetTip-Android
 * @FileName    : CameraContent
 * @Date        : 2024-04-23
 * @author      : CareBiz
 * @description : net.pettip.gps.app
 * @see net.pettip.gps.app.CameraContent
 */

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CameraContent(onExit: (Boolean) -> Unit, onRefresh: (Boolean) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val application = GPSApplication.instance

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    var takePhoto by remember{ mutableStateOf(false) }
    var currentUri by remember{ mutableStateOf<Uri?>(null) }

    val interactionSource1 = remember { MutableInteractionSource() }
    val interactionSource2 = remember { MutableInteractionSource() }
    val isPressed1 = interactionSource1.collectIsPressedAsState()
    val isPressed2 = interactionSource2.collectIsPressedAsState()
    val animateAlpha1 = animateFloatAsState(
        targetValue = if (isPressed1.value) 0.8f else 0.3f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    val animateAlpha2 = animateFloatAsState(
        targetValue = if (isPressed2.value) 0.8f else 0.3f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    DisposableEffect(Unit){
        onDispose { G.showCameraX = false }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { it ->
                    PreviewView(it).apply {
                        this.controller = controller
                        controller.bindToLifecycle(lifecycleOwner)
                    }
                },
                update = {
                },
                modifier = Modifier.fillMaxSize(),
            )

            Row (
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){

                RotateButton(
                    onClick = {
                        onExit(false)
                        G.showCameraX = false
                              },
                    icon = Icons.Default.Clear,
                    modifier = Modifier,
                    buttonSize = 50.dp,
                    iconSize = 20.dp
                )

                RotateButton(
                    modifier = Modifier,
                    onClick = {
                        takePhoto(
                            controller = controller,
                            context = context,
                            refresh = { newValue -> onRefresh(newValue) },
                            takePhoto = { newValue -> takePhoto = newValue },
                            currentUri = { newValue -> currentUri = newValue }
                        )
                    },
                    icon = ImageVector.vectorResource(id = R.drawable.icon_camera_map),
                    buttonSize = 80.dp,
                    iconSize = 40.dp
                )

                RotateButton(
                    onClick = {
                        controller.cameraSelector =
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else CameraSelector.DEFAULT_BACK_CAMERA
                    },
                    icon = Icons.Default.Refresh,
                    modifier = Modifier,
                    buttonSize = 50.dp,
                    iconSize = 20.dp
                )
            }
        } // Box

        AnimatedVisibility(
            visible = takePhoto,
            enter = EnterTransition.None,
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(currentUri)
                    .build(),
                filterQuality = FilterQuality.Low
            )
            
            Box(modifier = Modifier.fillMaxSize()){
                Image(
                    painter = painter,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth
                )
                
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Box (
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.Black.copy(animateAlpha1.value))
                            .clickable(
                                interactionSource = interactionSource1,
                                indication = null
                            ) {
                                deleteFileFromContentUri(currentUri,context)
                                takePhoto = false
                              },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "취 소",
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.White.copy(alpha = 0.4f)))

                    Box (
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.Black.copy(animateAlpha2.value))
                            .clickable(
                                interactionSource = interactionSource2,
                                indication = null
                            ) {
                                currentUri?.let { application.img(it) }
                                onRefresh(true)
                                takePhoto = false
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "선 택",
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }

            }

        }
    }
}

@Composable
fun RotateButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    iconSize: Dp = 40.dp,
    buttonSize: Dp = 80.dp,
    icon: ImageVector
){
    val mutableInteractionSource = remember {
        MutableInteractionSource()
    }
    val pressed = mutableInteractionSource.collectIsPressedAsState()
    val rotation = animateDpAsState(
        targetValue = if (pressed.value) {
            8.dp
        } else {
            0.dp
        },
        label = "elevation"
    )

    val alpha = animateFloatAsState(
        targetValue = if (pressed.value) {
            1f
        } else {
            0.7f
        },
        label = "alpha"
    )

    Box(
        modifier = modifier
            .size(buttonSize + 8.dp)
            .padding(8.dp - rotation.value)
            .background(color = Color.White.copy(alpha = alpha.value), shape = CircleShape)
            .border(BorderStroke(width = 1.dp, color = Color.Black), CircleShape)
            .graphicsLayer {
                this.rotationZ = rotation.value.toPx()
            }
            .clickable(
                interactionSource = mutableInteractionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.photo),
            tint = Color.Black,
            modifier = Modifier
                .size(iconSize)
                .alpha(0.7f)
        )
    }
}


private fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    refresh: (Boolean) -> Unit,
    takePhoto : (Boolean) -> Unit,
    currentUri : (Uri) -> Unit
) {
    val application = GPSApplication.instance

    val randomNumber = Random.nextInt(10000, 100000)
    val day = SimpleDateFormat("yyyyMMdd", Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "PETTIP_${day}_${randomNumber}")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PetTip")
        }
    }

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues)
        .build()

    // Set up image capture listener, which is triggered after photo has
    // been taken
    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("LOG", "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults){
                output.savedUri?.let {
                    //application.img(uri = it)
                    currentUri(it)
                    takePhoto(true)
                    //refresh(true)
                }
            }
        }
    )
}

fun deleteFileFromContentUri(uri: Uri?, context: Context) {
    try {
        val deletedRows = uri?.let { context.contentResolver.delete(it, null, null) }
        if (deletedRows != null) {
            if (deletedRows > 0) {
                Log.d("DELETE", "파일 삭제 성공: $uri")
            } else {
                Log.e("DELETE", "파일 삭제 실패: $uri")
            }
        }
    } catch (e: Exception) {
        Log.e("DELETE", "파일 삭제 중 예외 발생: $uri", e)
    }
}