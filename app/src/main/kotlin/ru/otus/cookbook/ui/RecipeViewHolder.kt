package ru.otus.cookbook.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.transform.CircleCropTransformation
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.data.loadImage
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
            loadImage(recipeAvatar, imageUrl, CircleCropTransformation())
            recipeTitle.text = title
            recipeDescription.text = description
            binding.root.setOnClickListener {
                itemListener.onItemClick(id)
            }
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
