package id.dhuwit.feature.dashboard.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.feature.dashboard.R
import id.dhuwit.feature.dashboard.databinding.DashboardFragmentBinding
import id.dhuwit.feature.dashboard.ui.account.DashboardAccountFragment
import id.dhuwit.feature.dashboard.ui.overview.DashboardOverviewFragment
import id.dhuwit.feature.transaction.router.TransactionRouter
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

    private var binding: DashboardFragmentBinding? = null

    private val transactionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val currentFragment =
                childFragmentManager.findFragmentById(binding?.frameLayout?.id ?: 0)

            when (currentFragment) {
                is DashboardOverviewFragment -> (currentFragment as DashboardOverviewFragment).updateDataOverview()
                is DashboardAccountFragment -> (currentFragment as DashboardAccountFragment).updateDataAccount()
            }
        }
    }

    @Inject
    lateinit var transactionRouter: TransactionRouter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun init() {
        // Set first menu
        showMenuOverview()
    }

    override fun listener() {
        binding?.let {
            it.buttonTransaction.setOnClickListener {
                openTransactionPage(null)
            }

            it.layoutOverview.setOnClickListener {
                showMenuOverview()
            }

            it.layoutAccount.setOnClickListener {
                showMenuAccount()
            }
        }
    }

    override fun observer() {

    }

    private fun showMenuOverview() {
        childFragmentManager.beginTransaction()
            .replace(
                binding?.frameLayout?.id ?: 0,
                DashboardOverviewFragment(),
                TAG_OVERVIEW
            )
            .commit()

        binding?.let {
            it.imageOverview.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_overview_white)
            )
            it.textOverview.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

            it.imageAccount.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_account_green)
            )
            it.textAccount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorMinorDark
                )
            )
        }
    }

    private fun showMenuAccount() {
        childFragmentManager.beginTransaction()
            .replace(
                binding?.frameLayout?.id ?: 0,
                DashboardAccountFragment(),
                TAG_ACCOUNT
            )
            .commit()

        binding?.let {
            it.imageAccount.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_account_white)
            )
            it.textAccount.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            it.imageOverview.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_overview_green)
            )
            it.textOverview.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorMinorDark
                )
            )
        }
    }

    private fun openTransactionPage(transactionId: Long?) {
        transactionResult.launch(
            transactionRouter.openTransactionPage(
                requireContext(),
                transactionId
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val TAG_OVERVIEW: String = "tag_overview"
        private const val TAG_ACCOUNT: String = "tag_account"
    }

}