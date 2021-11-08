package id.dhuwit.core.category.model

data class CategorySearch(
    val keywords: String,
    val categories: List<Category>?
)