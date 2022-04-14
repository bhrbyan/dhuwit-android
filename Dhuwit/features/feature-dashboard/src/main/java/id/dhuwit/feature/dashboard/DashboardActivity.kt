package id.dhuwit.feature.dashboard

import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.dashboard.databinding.DashboardActivityBinding
import id.dhuwit.feature.dashboard.ui.account.DashboardAccountFragment
import id.dhuwit.feature.dashboard.ui.overview.DashboardOverviewFragment

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    private lateinit var binding: DashboardActivityBinding

    override fun init() {
        binding = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init default menu
        showMenuOverview()
    }

    override fun listener() {
        with(binding) {
            buttonTransaction.setOnClickListener {
                val fragment = supportFragmentManager.findFragmentById(
                    binding.frameLayout?.id ?: 0
                ) as DashboardOverviewFragment

                fragment.openTransactionPage(null)
            }

            layoutOverview?.setOnClickListener {
                showMenuOverview()
            }

            layoutAccount?.setOnClickListener {
                showMenuAccount()
            }
        }
    }

    override fun observer() {

    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showMenuOverview() {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.frameLayout?.id ?: 0,
                DashboardOverviewFragment()
            )
            .commit()
    }

    private fun showMenuAccount() {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.frameLayout?.id ?: 0,
                DashboardAccountFragment()
            )
            .commit()
    }

}