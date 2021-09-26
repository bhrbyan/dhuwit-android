package id.dhuwit.feature.dashboard

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.dashboard.adapter.DashboardTransactionAdapter
import id.dhuwit.feature.dashboard.adapter.DashboardTransactionItemListener
import id.dhuwit.feature.dashboard.databinding.DashboardActivityBinding
import id.dhuwit.feature.transaction.router.TransactionRouter
import id.dhuwit.state.State
import id.dhuwit.storage.Storage
import id.dhuwit.uikit.widget.DividerMarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity(), DashboardTransactionItemListener {

    private lateinit var binding: DashboardActivityBinding
    private lateinit var adapterTransaction: DashboardTransactionAdapter
    private val viewModel: DashboardViewModel by viewModels()

    @Inject
    lateinit var transactionRouter: TransactionRouter

    @Inject
    lateinit var storage: Storage

    private val transactionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getDetails()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        setUpAdapter()
        viewModel.getDetails()

        binding.buttonTransaction.setOnClickListener {
            openTransactionPage(null)
        }

        observer()
    }

    override fun observer() {
        viewModel.details.observe(this) {
            when (it) {
                is State.Loading -> {
                    showLoading()
                }
                is State.Success -> {
                    setUpDataAccount(it.data?.account)
                    setUpDataTransaction(it.data?.transactions)
                    hideLoading()
                }
                is State.Error -> {
                    hideLoading()
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setUpAdapter() {
        adapterTransaction = DashboardTransactionAdapter(this, emptyList()).apply {
            listener = this@DashboardActivity
        }
        binding.recyclerViewTransaction.apply {
            adapter = adapterTransaction
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerMarginItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL,
                    resources.getDimensionPixelSize(R.dimen.uikit_margin_padding_size_medium)
                )
            )
        }
    }

    override fun onClickTransaction(transaction: Transaction?) {
        openTransactionPage(transaction?.id)
    }

    private fun setUpDataTransaction(transactions: List<Transaction>?) {
        if (transactions.isNullOrEmpty()) {
            showMessageEmptyTransaction()
        } else {
            showTransaction()
            adapterTransaction.updateList(transactions, storage.getSymbolCurrency())
        }
    }

    private fun setUpDataAccount(account: Account?) {
        with(binding) {
            textAccountName.text = account?.name
            textAccountBalance.text = account?.balance?.convertPriceWithCurrencyFormat(
                storage.getSymbolCurrency()
            )
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBarAccount.show()
            progressBarTransaction.show()
            textAccountName.gone()
            textAccountBalance.gone()
            imageArrowRight.gone()
        }
    }

    private fun hideLoading() {
        with(binding) {
            progressBarAccount.hide()
            progressBarTransaction.hide()
            textAccountName.visible()
            textAccountBalance.visible()
            imageArrowRight.visible()
        }
    }

    private fun showTransaction() {
        with(binding) {
            recyclerViewTransaction.visible()
            textEmptyTransaction.gone()
        }
    }

    private fun showMessageEmptyTransaction() {
        with(binding) {
            recyclerViewTransaction.gone()
            textEmptyTransaction.visible()
        }
    }

    private fun openTransactionPage(transactionId: Long?) {
        transactionResult.launch(transactionRouter.openTransactionPage(this, transactionId))
    }
}