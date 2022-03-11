package id.dhuwit.feature.account.ui.list

import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.account.ui.list.AccountListAdapter.Companion.VIEW_TYPE_ADD

sealed class AccountListViewType {

    abstract val id: Long?

    data class Item(val account: Account) : AccountListViewType() {
        override val id: Long? = account.id
    }

    object Add : AccountListViewType() {
        override val id: Long = VIEW_TYPE_ADD.toLong()
    }

}
