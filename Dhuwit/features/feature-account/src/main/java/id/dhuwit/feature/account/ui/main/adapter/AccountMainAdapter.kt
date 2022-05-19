package id.dhuwit.feature.account.ui.main.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.dhuwit.feature.account.ui.main.AccountMainFragment

class AccountMainAdapter(activity: AppCompatActivity, val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return AccountMainFragment()
    }
}