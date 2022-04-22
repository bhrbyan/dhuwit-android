package id.dhuwit.feature.transaction.ui.list

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionListType
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.transaction.R
import id.dhuwit.feature.transaction.databinding.TransactionListActivityBinding
import id.dhuwit.feature.transaction.router.TransactionRouter
import id.dhuwit.feature.transaction.router.TransactionRouterImpl
import id.dhuwit.feature.transaction.ui.list.adapter.TransactionListHeaderAdapter
import id.dhuwit.feature.transaction.ui.list.adapter.TransactionListItemListener
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class TransactionListActivity : BaseActivity(), TransactionListItemListener {

    private lateinit var binding: TransactionListActivityBinding
    private lateinit var adapterTransactionList: TransactionListHeaderAdapter
    private val viewModel: TransactionListViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var transactionRouter: TransactionRouter

    override fun init() {
        binding = TransactionListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        setUpAdapter()
        val periodDate: String? =
            intent.getStringExtra(TransactionRouterImpl.KEY_TRANSACTION_PERIOD_DATE)
        val transactionListType: TransactionListType =
            TransactionListType.convertToTransactionListType(
                intent.getStringExtra(TransactionRouterImpl.KEY_TRANSACTION_LIST_TYPE)
            )
        val transactionTypeString: String? =
            intent.getStringExtra(TransactionRouterImpl.KEY_TRANSACTION_TYPE)
        val transactionType = if (transactionTypeString.isNullOrEmpty()) {
            null
        } else {
            TransactionType.getTransactionType(transactionTypeString)
        }

        val categoryId: Long =
            intent.getLongExtra(TransactionRouterImpl.KEY_TRANSACTION_CATEGORY_ID, 0)

        setTextPeriodDate(periodDate)
        setTextTitle(transactionListType)

        viewModel.getTransactions(periodDate, transactionType, categoryId)
    }

    private fun setTextTitle(transactionListType: TransactionListType) {
        binding.textTitle.text = transactionListType.toString()
    }

    private fun setTextPeriodDate(periodDate: String?) {
        binding.textPeriodDate.text = periodDate
    }

    override fun listener() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is TransactionListViewState.GetTransactions -> {
                    adapterTransactionList.updateList(it.transactions, storage.getSymbolCurrency())
                    binding.textTotalAmountTransaction.text =
                        it.totalAmountTransaction.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
                    binding.textTotalTransaction.text = if (it.totalTransaction > 1) {
                        getString(
                            R.string.transactions_list_total_transaction_greater_than_one,
                            it.totalTransaction
                        )
                    } else {
                        getString(
                            R.string.transactions_list_total_transaction_lower_than_one,
                            it.totalTransaction
                        )
                    }
                }
                is ViewState.Error -> showError()
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.title = ""
    }

    private fun setUpAdapter() {
        adapterTransactionList = TransactionListHeaderAdapter().apply {
            listener = this@TransactionListActivity
        }
        binding.recyclerView.apply {
            adapter = adapterTransactionList
            layoutManager = LinearLayoutManager(this@TransactionListActivity)
        }
    }

    override fun onClickTransaction(transaction: Transaction?) {
        startActivity(transactionRouter.openTransactionPage(this, transaction?.id))
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}