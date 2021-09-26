package id.dhuwit.feature.category.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.category.CategoryListActivity
import id.dhuwit.feature.category.CategoryListConstants.KEY_CATEGORY_TYPE

internal object CategoryRouterImpl : CategoryRouter {
    override fun openCategoryListPage(context: Context, categoryType: String): Intent {
        return Intent(context, CategoryListActivity::class.java).apply {
            putExtra(KEY_CATEGORY_TYPE, categoryType)
        }
    }
}