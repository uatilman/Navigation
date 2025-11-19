package ru.otus.cookbook.data

import android.util.Log
import android.widget.ImageView
import coil.load
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.Transformation
import ru.otus.cookbook.R

fun loadImage(imageView: ImageView, imageUrl: String, vararg transformations: Transformation) {
    imageView.load(imageUrl) {
        setHeader("User-Agent", "Mozilla/5.0")
        placeholder(R.drawable.cart_item_icon)
        error(R.drawable.ic_launcher_background)
        transformations(*transformations) // Optional: Apply transformations
        memoryCachePolicy(CachePolicy.ENABLED)  // Optional: Enable memory caching
        diskCachePolicy(CachePolicy.ENABLED)    // Optional: Enable disk caching
        listener(
            onSuccess = { request: ImageRequest, result: SuccessResult ->
                Log.d("Coil", "Image loaded successfully from ${result.dataSource}")
            },
            onError = { request: ImageRequest, result: ErrorResult ->
                Log.e("Coil", "Image load failed: ${result.throwable.message}")
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }
        )
    }
}