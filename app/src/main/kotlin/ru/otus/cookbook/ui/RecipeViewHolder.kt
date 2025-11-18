package ru.otus.cookbook.ui

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.VhRecipeCategoryBinding
import ru.otus.cookbook.databinding.VhRecipeItemBinding


class RecipeViewHolder(
    private val binding: VhRecipeItemBinding,
    private val itemListener: ItemListener,
) : RecyclerView.ViewHolder(binding.root) {

    private val recipeAvatar: ImageView = binding.recipeAvatar
    private val recipeTitle: TextView = binding.recipeTitle
    private val recipeDescription: TextView = binding.recipeDescription

    fun bind(recipe: RecipeListItem.RecipeItem) {
        with(recipe) {
            loadImage()
            recipeTitle.text = title
            recipeDescription.text = description
            binding.root.setOnClickListener {
                itemListener.onItemClick(id)
            }
        }
    }

    private fun RecipeListItem.RecipeItem.loadImage() {
        recipeAvatar.load(imageUrl) {
            setHeader("User-Agent", "Mozilla/5.0")
            placeholder(R.drawable.cart_item_icon)
            error(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation()) // Optional: Apply transformations
            memoryCachePolicy(CachePolicy.ENABLED)  // Optional: Enable memory caching
            diskCachePolicy(CachePolicy.ENABLED)    // Optional: Enable disk caching
            listener(
                onSuccess = { request: ImageRequest, result: SuccessResult ->
                    Log.d("Coil", "Image loaded successfully from ${result.dataSource}")
                },
                onError = { request: ImageRequest, result: ErrorResult ->
                    Log.e("Coil", "Image load failed: ${result.throwable.message}")
                    recipeAvatar.setImageResource(R.drawable.ic_launcher_background)
                }
            )
        }
    }
}


class CategoryViewHolder(
    binding: VhRecipeCategoryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val categoryName: TextView = binding.categoryName


    fun bind(category: RecipeListItem.CategoryItem) {
        with(category) {
            categoryName.text = name
        }
    }
}
