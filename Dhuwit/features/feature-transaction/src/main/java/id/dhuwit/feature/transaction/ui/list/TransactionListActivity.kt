package id.dhuwit.feature.transaction.ui.list

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
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

    private var periodDate: String? = null
    private var transactionType: TransactionType? = null
    private var categoryId: Long? = null
    private var accountId: Long? = null

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var transactionRouter: TransactionRouter

    private val transactionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getTransactions(periodDate, transactionType, categoryId, accountId)
        }
    }

    override fun init() {
        binding = TransactionListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        setUpAdapter()

        periodDate = intent.getStringExtra(TransactionRouterImpl.KEY_TRANSACTION_PERIOD_DATE)

        val transactionListType: TransactionListType =
            TransactionListType.convertToTransactionListType(
                intent.getStringExtra(TransactionRouterImpl.KEY_TRANSACTION_LIST_TYPE)
            )
        val transactionTypeString: String? =
            intent.getStringExtra(TransactionRouterImpl.KEY_TRANSACTION_TYPE)
        transactionType = if (transactionTypeString.isNullOrEmpty()) {
            null
        } else {
            TransactionType.getTransactionType(transactionTypeString)
        }

        categoryId =
            intent.getLongExtra(TransactionRouterImpl.KEY_TRANSACTION_CATEGORY_ID, DEFAULT_VALUE_ID)

        accountId =
            intent.getLongExtra(TransactionRouterImpl.KEY_TRANSACTION_ACCOUNT_ID, DEFAULT_VALUE_ID)

        setTextPeriodDate(periodDate)
        setTextTitle(transactionListType)

        viewModel.getTransactions(periodDate, transactionType, categoryId, accountId)
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
                    setTextTotalAmountTransaction(it.totalAmountTransaction)
                    setTextTotalTransactions(it.totalTransaction)
                    if (it.transactions.isNullOrEmpty()) {
                        showEmptyState()
                    } else {
                        showList(it.transactions)
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
        transactionResult.launch(
            transactionRouter.openTransactionPage(
                this,
                transaction?.id
            )
        )
    }

    private fun setTextTotalTransactions(totalTransaction: Int) {
        binding.textTotalTransaction.text = if (totalTransaction > 1) {
            getString(
                R.string.transactions_list_total_transaction_greater_than_one,
                totalTransaction
            )
        } else {
            getString(
                R.string.transactions_list_total_transaction_lower_than_one,
                totalTransaction
            )
        }
    }

    private fun setTextTotalAmountTransaction(totalAmountTransaction: Double) {
        binding.textTotalAmountTransaction.text =
            totalAmountTransaction.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
    }

    private fun showList(transactions: List<Transaction>) {
        binding.textEmptyTransaction.gone()
        adapterTransactionList.updateList(transactions, storage.getSymbolCurrency())
    }

    private fun showEmptyState() {
        binding.recyclerView.gone()
        binding.textEmptyTransaction.visible()
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val DEFAULT_VALUE_ID: Long = -1
    }
}