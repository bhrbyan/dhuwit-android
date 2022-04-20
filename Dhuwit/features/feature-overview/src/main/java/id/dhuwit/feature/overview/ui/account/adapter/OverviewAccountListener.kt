package id.dhuwit.feature.overview.ui.account.adapter

import id.dhuwit.core.transaction.model.TransactionAccount

interface OverviewAccountListener {
    fun onClickAccount(item: TransactionAccount)
}