package id.dhuwit.feature.category.adapter

import id.dhuwit.core.category.model.Category

interface CategoryListListener {
    fun onSelectCategory(category: Category)
}