package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.launch
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.FragmentCookbookBinding

/**
 * Фрагмент для отображения списка рецептов
 */
class CookbookFragment : Fragment(), ItemListener {

    private val binding = FragmentBindingDelegate<FragmentCookbookBinding>(this)
    private val model: CookbookFragmentViewModel by viewModels { CookbookFragmentViewModel.Factory }

    private val recipeListDiffAdapter: RecipeListDiffAdapter by lazy { RecipeListDiffAdapter(this) }


    /**
     * Создает и возвращает представление фрагмента
     *
     * @param inflater Объект LayoutInflater для создания представления
     * @param container Родительская ViewGroup
     * @param savedInstanceState Сохраненное состояние фрагмента
     * @return Представление фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentCookbookBinding::inflate
    )

    /**
     * Вызывается после создания представления фрагмента
     *
     * @param view Представление фрагмента
     * @param savedInstanceState Сохраненное состояние фрагмента
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipeList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::onRecipeListUpdated)
        }
    }

    /**
     * Настраивает RecyclerView для отображения списка рецептов
     */
    private fun setupRecyclerView() = binding.withBinding {
        recycleView.addItemDecoration(
            DividerItemDecoration(
                this@CookbookFragment.requireActivity(),
                LinearLayout.VERTICAL
            )
        )
        recycleView.adapter = recipeListDiffAdapter
        model.recipeList
    }

    /**
     * Обновляет список рецептов в адаптере
     *
     * @param recipeList Новый список рецептов для отображения
     */
    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        recipeListDiffAdapter.submitList(recipeList)
    }

    /**
     * Обрабатывает нажатие на элемент списка рецептов
     *
     * @param id Идентификатор выбранного рецепта
     */
    override fun onItemClick(id: Int) {
        Toast.makeText(requireContext(), "Clicked $id", Toast.LENGTH_SHORT).show()
    }

    /**
     * Обрабатывает свайп по элементу списка рецептов
     *
     * @param id Идентификатор рецепта, по которому был совершен свайп
     */
    override fun onSwipe(id: Int) {
        Toast.makeText(requireContext(), "Swiped $id", Toast.LENGTH_SHORT).show()
    }
}