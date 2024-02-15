package net.pettip.app.navi.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomIndicator(
    modifier: Modifier = Modifier,
    state: PullRefreshState,
    refreshing: Boolean
) {
    val indicatorSize = 40.dp

    Surface(
        modifier = modifier
            .size(indicatorSize)
            .pullRefreshIndicatorTransform(state, true),
        shape = CircleShape,
        elevation = if (refreshing) 16.dp else 0.dp
    ) {
        if (refreshing) {
            val transition = rememberInfiniteTransition(label = "")
            val degree by transition.animateFloat(
                initialValue = 0f, targetValue = 360f, label = "",animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        easing = LinearEasing,
                    )
                )
            )
            PokeBall(modifier = Modifier.rotate(degree), indicatorSize)
        } else {
            PokeBall(modifier = Modifier.rotate(state.progress * 180), indicatorSize)
        }
    }
}

@Composable
fun PokeBall(modifier: Modifier = Modifier, size: Dp) {
    Canvas(modifier = modifier) {
        val length = size.toPx()
        val innerCircle = (size / 10).toPx()
        val strokeWidth = (size / 10).toPx()

        drawRect(color = Color.Red, size = Size(width = length, height = length / 2))
        drawRect(
            color = Color.White,
            topLeft = Offset(0f, length / 2),
            size = Size(width = length, height = length / 2)
        )
        drawCircle(color = Color.Black, radius = innerCircle, style = Stroke(strokeWidth))
        drawCircle(color = Color.White, radius = innerCircle, style = Fill)
        drawCircle(color = Color.Black, style = Stroke(strokeWidth))
    }
}
