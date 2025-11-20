package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
import ru.otus.cookbook.ui.dialog.DeleteConfirmationDialog.Companion.CONFIRMATION_RESULT

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
        setupAlertDeleteResult()
        binding.withBinding {
            topAppBar.setNavigationOnClickListener(::navigateBackToCookBook)
            topAppBar.setOnMenuItemClickListener(::navigateToRemoveDialog)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    private fun navigateToRemoveDialog(menuItem: MenuItem): Boolean =
        if (menuItem.itemId == R.id.menu_delete) {
            findNavController()
                .navigate(RecipeFragmentDirections.actionOpenDeleteConfirmationDialog(getTitle()))
            true
        } else false


    private fun navigateBackToCookBook(v: View?) {
        findNavController().navigate(RecipeFragmentDirections.actionBackToCookbook())
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

    private fun setupAlertDeleteResult() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recipeFragment)
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                if (navBackStackEntry.savedStateHandle.contains(CONFIRMATION_RESULT)) {
                    if (true == navBackStackEntry.savedStateHandle.get<Boolean>(CONFIRMATION_RESULT))
                        deleteRecipe()
                    findNavController().popBackStack()
                }
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun deleteRecipe() {
        model.delete()
    }
}