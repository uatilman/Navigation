package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.data.loadImage
import ru.otus.cookbook.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private val args: RecipeFragmentArgs by navArgs()
    private val recipeId: Int get() = args.recipeId

    private val binding = FragmentBindingDelegate<FragmentRecipeBinding>(this)
    private val model: RecipeFragmentViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                set(RecipeFragmentViewModel.RECIPE_ID_KEY, recipeId)
            }
        },
        factoryProducer = { RecipeFragmentViewModel.Factory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(container, FragmentRecipeBinding::inflate)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.withBinding {
            topAppBar.setNavigationOnClickListener {
                findNavController().navigate(RecipeFragmentDirections.actionBackToCookbook())
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun displayRecipe(recipe: Recipe) {
        binding.withBinding {
            if (recipe.imageUrl.isNotEmpty()) {
                loadImage(recipeAvatar, recipe.imageUrl, RoundedCornersTransformation())
            } else {
                recipeAvatar.setImageResource(R.drawable.cart_item_icon)
            }

            recipeTitle.text = recipe.title.ifEmpty { getString(R.string.no_title) }
            recipeDescription.text =
                recipe.description.ifEmpty { getString(R.string.no_description) }

            recipeStep.text = if (recipe.steps.isNotEmpty()) {
                recipe.steps.joinToString("\n • ", "• ")
            } else {
                getString(R.string.no_steps)
            }
        }
    }

    private fun deleteRecipe() {
        model.delete()
    }
}