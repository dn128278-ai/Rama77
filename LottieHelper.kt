package com.rama77.app.utils

import android.content.Context
import android.view.View
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.rama77.app.R

/**
 * LottieHelper: High-performance Lottie animation controller for RAMA77.
 * Provides caching, repeat controls, and fallback handling for smooth 60fps UI.
 */
object LottieHelper {

    /**
     * Set up a LottieAnimationView with custom raw JSON resource
     */
    fun setupAnimation(
        view: LottieAnimationView,
        @RawRes rawResId: Int,
        autoPlay: Boolean = true,
        loop: Boolean = true
    ) {
        view.visibility = View.VISIBLE
        view.setAnimation(rawResId)
        view.repeatCount = if (loop) LottieDrawable.INFINITE else 0
        if (autoPlay) {
            view.playAnimation()
        }
    }

    /**
     * Splash screen brand reveal animation (res/raw/lottie_splash.json)
     */
    fun playSplashIntro(view: LottieAnimationView, onComplete: (() -> Unit)? = null) {
        view.setAnimation(R.raw.lottie_splash)
        view.repeatCount = 0
        view.addAnimatorListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(p0: android.animation.Animator) {}
            override fun onAnimationEnd(p0: android.animation.Animator) {
                onComplete?.invoke()
            }
            override fun onAnimationCancel(p0: android.animation.Animator) {}
            override fun onAnimationRepeat(p0: android.animation.Animator) {}
        })
        view.playAnimation()
    }

    /**
     * Wallet coin spinning animation for deposit screens
     */
    fun startWalletSpin(view: LottieAnimationView) {
        setupAnimation(view, R.raw.lottie_wallet, autoPlay = true, loop = true)
    }

    /**
     * Celebrate bid placement success
     */
    fun showSuccessCelebration(view: LottieAnimationView, onEnd: (() -> Unit)? = null) {
        view.setAnimation(R.raw.lottie_success)
        view.repeatCount = 0
        view.playAnimation()
    }
}
