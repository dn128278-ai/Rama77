package com.rama77.app.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rama77.app.R

/**
 * ImageLoaderHelper: Glide-based image loading engine for RAMA77.
 * Handles high-resolution market banners, game bazaar graphics, and user profiles.
 */
object ImageLoaderHelper {

    /**
     * Load image URL or drawable into ImageView with fade animation and cache strategy
     */
    fun loadImage(
        context: Context,
        imageUrl: String?,
        targetView: ImageView,
        placeholderResId: Int = R.drawable.ic_glide_placeholder,
        errorResId: Int = R.drawable.ic_glide_error
    ) {
        Glide.with(context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(placeholderResId)
                    .error(errorResId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(targetView)
    }

    /**
     * Load circular user avatar for Drawer header & settings
     */
    fun loadCircularAvatar(
        context: Context,
        avatarUrl: String?,
        targetView: ImageView
    ) {
        Glide.with(context)
            .load(avatarUrl)
            .apply(
                RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.bg_avatar_circle)
                    .error(R.drawable.bg_avatar_circle)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(targetView)
    }

    /**
     * Preload critical game banners to avoid UI jank during navigation
     */
    fun preloadBanner(context: Context, bannerUrl: String) {
        Glide.with(context)
            .load(bannerUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .preload()
    }
}
