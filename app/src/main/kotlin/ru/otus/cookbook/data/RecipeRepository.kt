package ru.otus.cookbook.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * Предоставляет доступ к списку рецептов.
 */
class RecipeRepository(recipes: List<Recipe>) {

    private val nextId = recipes.maxOfOrNull { it.id }?.plus(1) ?: 1
    private val recipes = MutableStateFlow(recipes)

    /**
     * Возвращает список рецептов в виде потока.
     * @param scope Область видимости корутины для потока.
     * @param filter Фильтр для применения к рецептам.
     */
    suspend fun getRecipes(scope: CoroutineScope, filter: RecipeFilter): StateFlow<List<Recipe>> = recipes
        .map { recipes -> recipes.asSequence()
            .filter { filter.categories.isEmpty() || filter.categories.contains(it.category) }
            .filter { filter.query.isNullOrBlank() || it.title.contains(filter.query, ignoreCase = true) }
            .toList()
        }
        .stateIn(scope)

    /**
     * Возвращает список категорий в виде потока.
     * @param scope Область видимости корутины для потока.
     */
    suspend fun getCategories(scope: CoroutineScope): StateFlow<List<RecipeCategory>> = recipes
        .map { recipes ->  recipes.map { it.category }.distinct().sorted() }
        .stateIn(scope)

    /**
     * Возвращает рецепт по указанному идентификатору.
     * @param id Идентификатор рецепта.
     * @return Рецепт с указанным идентификатором или null, если рецепт не найден.
     */
    fun getRecipe(id: Int): Recipe? = recipes.value.find { it.id == id }

    /**
     * Удаляет рецепт с указанным идентификатором.
     * @param id Идентификатор рецепта для удаления.
     */
    fun deleteRecipe(id: Int) {
        recipes.update { list ->
            list.filter { it.id != id }
        }
    }

    /**
     * Добавляет новый рецепт в список.
     * @param recipe Рецепт для добавления.
     */
    fun addRecipe(recipe: Recipe) {
        recipes.update { list ->
            list + recipe.copy(id = nextId)
        }
    }
}
