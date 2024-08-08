package com.xhand.hnu.components.chart

import android.text.Layout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberLayeredComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShadow
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.shape.markerCornered
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasureContext
import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
import com.patrykandpatrick.vico.core.cartesian.Insets
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.copyColor
import com.patrykandpatrick.vico.core.common.shape.Corner
import com.patrykandpatrick.vico.core.common.shape.Shape

@Composable
internal fun rememberMarker(
  labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
  showIndicator: Boolean = true,
): CartesianMarker {
  val labelBackgroundShape = Shape.markerCornered(Corner.FullyRounded)
  val labelBackground =
    rememberShapeComponent(
      color = MaterialTheme.colorScheme.surfaceBright,
      shape = labelBackgroundShape,
      shadow =
        rememberShadow(
          radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP.dp,
          dy = LABEL_BACKGROUND_SHADOW_DY_DP.dp,
        ),
    )
  val label =
    rememberTextComponent(
      color = MaterialTheme.colorScheme.onSurface,
      textAlignment = Layout.Alignment.ALIGN_CENTER,
      padding = Dimensions.of(8.dp, 4.dp),
      background = labelBackground,
      minWidth = TextComponent.MinWidth.fixed(40.dp),
    )
  val indicatorFrontComponent =
    rememberShapeComponent(MaterialTheme.colorScheme.surface, Shape.Pill)
  val indicatorCenterComponent = rememberShapeComponent(shape = Shape.Pill)
  val indicatorRearComponent = rememberShapeComponent(shape = Shape.Pill)
  val indicator =
    rememberLayeredComponent(
      rear = indicatorRearComponent,
      front =
        rememberLayeredComponent(
          rear = indicatorCenterComponent,
          front = indicatorFrontComponent,
          padding = Dimensions.of(5.dp),
        ),
      padding = Dimensions.of(10.dp),
    )
  val guideline = rememberAxisGuidelineComponent()
  return remember(label, labelPosition, indicator, showIndicator, guideline) {
    object :
      DefaultCartesianMarker(
        label = label,
        labelPosition = labelPosition,
        indicator =
          if (showIndicator) {
            { color ->
              LayeredComponent(
                rear = ShapeComponent(color.copyColor(alpha = 0.15f), Shape.Pill),
                front =
                  LayeredComponent(
                    rear =
                      ShapeComponent(
                        color = color,
                        shape = Shape.Pill,
                        shadow = Shadow(radiusDp = 12f, color = color),
                      ),
                    front = indicatorFrontComponent,
                    padding = Dimensions.of(5.dp),
                  ),
                padding = Dimensions.of(10.dp),
              )
            }
          } else {
            null
          },
        indicatorSizeDp = 36f,
        guideline = guideline,
      ) {
      override fun updateInsets(
        context: CartesianMeasureContext,
        horizontalDimensions: HorizontalDimensions,
        model: CartesianChartModel,
        insets: Insets,
      ) {
        with(context) {
          val baseShadowInsetDp =
            CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP
          var topInset = (baseShadowInsetDp - LABEL_BACKGROUND_SHADOW_DY_DP).pixels
          var bottomInset = (baseShadowInsetDp + LABEL_BACKGROUND_SHADOW_DY_DP).pixels
          when (labelPosition) {
            LabelPosition.Top,
            LabelPosition.AbovePoint -> topInset += label.getHeight(context) + tickSizeDp.pixels
            LabelPosition.Bottom -> bottomInset += label.getHeight(context) + tickSizeDp.pixels
            LabelPosition.AroundPoint -> {}
          }
          insets.ensureValuesAtLeast(top = topInset, bottom = bottomInset)
        }
      }
    }
  }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f
