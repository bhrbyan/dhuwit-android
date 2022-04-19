package id.dhuwit.feature.overview.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.feature.account.ui.list.AccountListFragment
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewFragmentBinding
import id.dhuwit.feature.overview.ui.account.OverviewAccountFragment
import id.dhuwit.feature.overview.ui.category.OverviewCategoryFragment
import id.dhuwit.feature.overview.ui.transaction.OverviewTransactionFragment
import id.dhuwit.feature.transaction.router.TransactionRouter
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : BaseFragment() {

    private var binding: OverviewFragmentBinding? = null

    private val transactionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val currentFragment =
                childFragmentManager.findFragmentById(binding?.frameLayout?.id ?: 0)

            when (currentFragment) {
                is OverviewTransactionFragment -> (currentFragment as OverviewTransactionFragment).updateDataOverview()
                is OverviewCategoryFragment -> (currentFragment as OverviewCategoryFragment).updateDataCategories()
                is AccountListFragment -> (currentFragment as AccountListFragment).updateDataAccount()
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
        binding = OverviewFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun init() {
        // ToDo: Update implementation so when device rotate not back to transaction
        showMenuTransaction()
    }

    override fun listener() {
        binding?.let {
            it.buttonTransaction.setOnClickListener {
                openTransactionPage(null)
            }

            it.layoutMenuTransaction.setOnClickListener {
                showMenuTransaction()
            }

            it.layoutMenuCategory.setOnClickListener {
                showMenuCategory()
            }

            it.layoutMenuAccount.setOnClickListener {
                showMenuAccount()
            }
        }
    }

    override fun observer() {

    }

    private fun showMenuTransaction() {
        setSelectedMenu(TAG_TRANSACTION)
        childFragmentManager.beginTransaction()
            .replace(
                binding?.frameLayout?.id ?: 0,
                OverviewTransactionFragment(),
                TAG_TRANSACTION
            )
            .commit()
    }

    private fun showMenuCategory() {
        setSelectedMenu(TAG_CATEGORY)
        childFragmentManager.beginTransaction()
            .replace(
                binding?.frameLayout?.id ?: 0,
                OverviewCategoryFragment(),
                TAG_CATEGORY
            )
            .commit()
    }

    private fun showMenuAccount() {
        setSelectedMenu(TAG_ACCOUNT)
        childFragmentManager.beginTransaction()
            .replace(
                binding?.frameLayout?.id ?: 0,
                OverviewAccountFragment(),
                TAG_ACCOUNT
            )
            .commit()
    }

    fun openTransactionPage(transactionId: Long?) {
        transactionResult.launch(
            transactionRouter.openTransactionPage(
                requireContext(),
                transactionId
            )
        )
    }

    private fun setSelectedMenu(tag: String) {
        var imageTransaction = 0
        var textColorTransaction = 0

        var imageCategory = 0
        var textColorCategory = 0

        var imageAccount = 0
        var textColorAccount = 0

        when (tag) {
            TAG_TRANSACTION -> {
                imageTransaction = R.drawable.ic_transaction_white
                textColorTransaction = R.color.white

                imageCategory = R.drawable.ic_category_green
                textColorCategory = R.color.colorMinorDark

                imageAccount = R.drawable.ic_account_green
                textColorAccount = R.color.colorMinorDark

            }
            TAG_CATEGORY -> {
                imageTransaction = R.drawable.ic_transaction_green
                textColorTransaction = R.color.colorMinorDark

                imageCategory = R.drawable.ic_category_white
                textColorCategory = R.color.white

                imageAccount = R.drawable.ic_account_green
                textColorAccount = R.color.colorMinorDark
            }
            TAG_ACCOUNT -> {
                imageTransaction = R.drawable.ic_transaction_green
                textColorTransaction = R.color.colorMinorDark

                imageCategory = R.drawable.ic_category_green
                textColorCategory = R.color.colorMinorDark

                imageAccount = R.drawable.ic_account_white
                textColorAccount = R.color.white
            }
        }

        binding?.let {
            it.imageMenuTransaction.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), imageTransaction)
            )
            it.textMenuTransaction.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    textColorTransaction
                )
            )

            it.imageMenuCategory.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), imageCategory)
            )
            it.textMenuCategory.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    textColorCategory
                )
            )

            it.imageMenuAccount.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), imageAccount)
            )
            it.textMenuAccount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    textColorAccount
                )
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val TAG_TRANSACTION: String = "tag_transaction"
        private const val TAG_CATEGORY: String = "tag_category"
        private const val TAG_ACCOUNT: String = "tag_account"
    }

}