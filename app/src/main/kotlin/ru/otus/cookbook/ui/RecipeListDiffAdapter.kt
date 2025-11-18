package ru.otus.cookbook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.VhRecipeCategoryBinding
import ru.otus.cookbook.databinding.VhRecipeItemBinding

/**
 * Адаптер для отображения списка рецептов с использованием DiffUtil для оптимизации обновлений
 */
class RecipeListDiffAdapter(
    private val itemListener: ItemListener,
) : ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(DiffUtilItem()) {

    /**
     * Создает ViewHolder для элемента списка в зависимости от типа элемента
     *
     * @param parent Родительская ViewGroup
     * @param viewType Тип представления элемента
     * @return ViewHolder для соответствующего типа элемента
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            RecipeListItem.RecipeItem.layoutId ->
                RecipeViewHolder(VhRecipeItemBinding.inflate(inflater, parent, false), itemListener)

            RecipeListItem.CategoryItem.layoutId ->
                CategoryViewHolder(VhRecipeCategoryBinding.inflate(inflater, parent, false))

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    /**
     * Привязывает данные элемента к ViewHolder
     *
     * @param holder ViewHolder для привязки данных
     * @param position Позиция элемента в списке
     */
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (val item = getItem(position)) {
            is RecipeListItem.RecipeItem -> (holder as RecipeViewHolder).bind(item)
            is RecipeListItem.CategoryItem -> (holder as CategoryViewHolder).bind(item)
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    /**
     * Возвращает тип представления для элемента в указанной позиции
     *
     * @param position Позиция элемента в списке
     * @return Тип представления элемента
     */
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecipeListItem.RecipeItem -> RecipeListItem.RecipeItem.layoutId
            is RecipeListItem.CategoryItem -> RecipeListItem.CategoryItem.layoutId
            else -> -1
        }
    }
}

/**
 * Класс для сравнения элементов списка при обновлении данных
 */
private class DiffUtilItem : DiffUtil.ItemCallback<RecipeListItem>() {

    /**
     * Проверяет, представляют ли два объекта один и тот же элемент
     *
     * @param oldItem Старый элемент
     * @param newItem Новый элемент
     * @return true, если элементы представляют один и тот же объект, иначе false
     */
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem::class == newItem::class && oldItem.layoutId == newItem.layoutId
    }

    /**
     * Проверяет, содержат ли два объекта одинаковые данные
     *
     * @param oldItem Старый элемент
     * @param newItem Новый элемент
     * @return true, если данные элементов идентичны, иначе false
     */
    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem == newItem
    }
}
