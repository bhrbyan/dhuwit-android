package id.dhuwit.feature.overview.ui.category.adapter

import id.dhuwit.core.transaction.model.TransactionCategory

interface OverviewCategoryListener {
    fun onClickCategory(item: TransactionCategory)
}