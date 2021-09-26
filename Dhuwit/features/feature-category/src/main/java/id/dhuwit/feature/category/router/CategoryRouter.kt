package id.dhuwit.feature.category.router

import android.content.Context
import android.content.Intent

interface CategoryRouter {
    fun openCategoryListPage(context: Context, categoryType: String): Intent
}