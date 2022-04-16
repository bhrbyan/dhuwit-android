package id.dhuwit.feature.main

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.dashboard.ui.dashboard.DashboardFragment
import id.dhuwit.feature.dashboard.ui.overview.DashboardOverviewFragment
import id.dhuwit.feature.main.databinding.MainActivityBinding

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: MainActivityBinding

    override fun init() {
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openMenu(DashboardFragment())
    }

    override fun listener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_dashboard -> {
                    openMenu(DashboardFragment())
                }
                R.id.menu_budget -> {
                    // Change fragment when budget ready
                    openMenu(DashboardOverviewFragment())
                }
                else -> throw Exception("Menu Item Not Found!")
            }

            true
        }
    }

    override fun observer() {

    }

    private fun openMenu(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .commit()
    }
}