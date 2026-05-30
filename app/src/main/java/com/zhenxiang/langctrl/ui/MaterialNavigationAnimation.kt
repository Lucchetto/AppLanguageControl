package com.zhenxiang.langctrl.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.PathEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * Navigation animations that replicate HyperOS 2 system activity animations
 */
object MaterialNavigationAnimation {

    private val fastOutExtraSlowInInterpolator get() = PathEasing(
        Path().apply {
            moveTo(0f, 0f)
            cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
            cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
        }
    )
    private val slideAnimationSpec = tween<IntOffset>(
        durationMillis = 450,
        easing = fastOutExtraSlowInInterpolator,
    )
    private val popSlideAnimationSpec = tween<IntOffset>(
        durationMillis = 450,
        easing = fastOutExtraSlowInInterpolator,
    )

    val enterTransition: (AnimatedContentTransitionScope<*>.() -> EnterTransition) = {
        slideInHorizontally(
            animationSpec = slideAnimationSpec,
            initialOffsetX = { it }
        )
    }

    val exitTransition: (AnimatedContentTransitionScope<*>.() -> ExitTransition) = {
        slideOutHorizontally(
            animationSpec = slideAnimationSpec,
            targetOffsetX = { (-.25f * it).roundToInt() }
        )
    }

    val popEnterTransition: (AnimatedContentTransitionScope<*>.() -> EnterTransition) = {
        slideInHorizontally(
            animationSpec = popSlideAnimationSpec,
            initialOffsetX = { (-.25f * it).roundToInt() }
        )
    }

    val popExitTransition: (AnimatedContentTransitionScope<*>.() -> ExitTransition) = {
        slideOutHorizontally(
            animationSpec = popSlideAnimationSpec,
            targetOffsetX = { it }
        )
    }

    fun hasPopAnimation(animations: List<Transition<*>.TransitionAnimationState<*, *>>): Boolean =
        animations.any { it.animationSpec === popSlideAnimationSpec }

    fun hasAnimation(animations: List<Transition<*>.TransitionAnimationState<*, *>>): Boolean =
        animations.any { it.animationSpec === slideAnimationSpec }
}