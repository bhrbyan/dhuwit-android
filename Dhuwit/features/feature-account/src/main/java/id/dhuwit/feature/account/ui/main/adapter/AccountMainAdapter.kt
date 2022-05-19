package id.dhuwit.feature.account.ui.main.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.account.ui.main.AccountMainFragment

class AccountMainAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {

    private var accounts: List<Account> = emptyList()

    fun updateAccounts(accounts: List<Account>) {
        this.accounts = accounts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun createFragment(position: Int): Fragment {
        return AccountMainFragment.newInstance(
            accounts[position].id ?: 0
        )
    }
}