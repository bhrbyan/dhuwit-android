package id.dhuwit.feature.dashboard.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.dashboard.R
import id.dhuwit.feature.dashboard.databinding.DashboardActivityBinding
import id.dhuwit.feature.dashboard.ui.account.DashboardAccountFragment
import id.dhuwit.feature.dashboard.ui.overview.DashboardOverviewFragment
import id.dhuwit.feature.transaction.router.TransactionRouter
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    private lateinit var binding: DashboardActivityBinding

    private val transactionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val currentFragment =
                supportFragmentManager.findFragmentById(binding.frameLayout?.id ?: 0)

            when (currentFragment) {
                is DashboardOverviewFragment -> (currentFragment as DashboardOverviewFragment).updateDataOverview()
                is DashboardAccountFragment -> (currentFragment as DashboardAccountFragment).updateDataAccount()
            }
        }
    }

    @Inject
    lateinit var transactionRouter: TransactionRouter

    override fun init() {
        binding = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init default menu
        showMenuOverview()
    }

    override fun listener() {
        with(binding) {
            buttonTransaction.setOnClickListener {
                openTransactionPage(null)
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
                DashboardOverviewFragment(),
                TAG_OVERVIEW
            )
            .commit()

        binding.imageOverview.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.ic_overview_white)
        )
        binding.textOverview?.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.imageAccount?.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.ic_account_blue)
        )
        binding.textAccount?.setTextColor(ContextCompat.getColor(this, R.color.congress_blue))
    }

    private fun showMenuAccount() {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.frameLayout?.id ?: 0,
                DashboardAccountFragment(),
                TAG_ACCOUNT
            )
            .commit()
        binding.imageAccount?.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.ic_account_white)
        )
        binding.textAccount?.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.imageOverview.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.ic_overview_blue)
        )
        binding.textOverview?.setTextColor(ContextCompat.getColor(this, R.color.congress_blue))
    }

    fun openTransactionPage(transactionId: Long?) {
        transactionResult.launch(
            transactionRouter.openTransactionPage(
                this,
                transactionId
            )
        )
    }

    companion object {
        private const val TAG_OVERVIEW: String = "tag_overview"
        private const val TAG_ACCOUNT: String = "tag_account"
    }

}