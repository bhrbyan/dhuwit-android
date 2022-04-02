package id.dhuwit.feature.dashboard

import android.content.res.Configuration
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.account.ui.list.AccountListAdapter
import id.dhuwit.feature.account.ui.list.AccountListListener
import id.dhuwit.feature.account.ui.list.AccountListViewModel
import id.dhuwit.feature.account.ui.list.AccountListViewState
import id.dhuwit.feature.dashboard.adapter.DashboardTransactionAdapter
import id.dhuwit.feature.dashboard.adapter.DashboardTransactionItemListener
import id.dhuwit.feature.dashboard.databinding.DashboardActivityBinding
import id.dhuwit.feature.transaction.router.TransactionRouter
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import id.dhuwit.uikit.divider.DividerMarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity(), DashboardTransactionItemListener, AccountListListener {

    private lateinit var binding: DashboardActivityBinding
    private lateinit var adapterTransaction: DashboardTransactionAdapter
    private lateinit var adapterAccount: AccountListAdapter

    private val viewModelDashboard: DashboardViewModel by viewModels()
    private val viewModelAccountList: AccountListViewModel by viewModels()

    @Inject
    lateinit var transactionRouter: TransactionRouter

    @Inject
    lateinit var accountRouter: AccountRouter

    @Inject
    lateinit var storage: Storage

    private val transactionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModelDashboard.getDetails()
            viewModelAccountList.getAccounts()
        }
    }

    private val accountResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModelAccountList.getAccounts()
        }
    }

    override fun init() {
        binding = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        setUpAdapterAccount()
        setUpAdapterTransaction()

        viewModelDashboard.setDefaultPeriodDate()
        viewModelDashboard.getDetails()
    }

    override fun listener() {
        with(binding) {
            buttonTransaction.setOnClickListener {
                openTransactionPage(null)
            }

            imageNext.setOnClickListener {
                viewModelDashboard.onNextPeriodDate()
            }

            imagePrevious.setOnClickListener {
                viewModelDashboard.onPreviousPeriodDate()
            }
        }
    }

    override fun observer() {
        viewModelDashboard.viewState.observe(this@DashboardActivity) {
            when (it) {
                is DashboardViewState.GetDetails -> {
                    setUpDataTransaction(it.dashboard.transactions)
                    setUpDataOverview(it.dashboard.overviewIncome, it.dashboard.overviewExpense)
                }
                is DashboardViewState.TransactionNotFound -> {
                    showError(
                        getString(R.string.dashboard_transactions_not_found)
                    )
                }
                is DashboardViewState.SetPeriodDate -> {
                    binding.textMonth.text = it.periodDate
                }
                is ViewState.Error -> {
                    showError(
                        getString(R.string.general_error_message)
                    )
                }
            }
        }

        viewModelAccountList.viewState.observe(this) {
            when (it) {
                is AccountListViewState.GetAccounts -> {
                    val sortedAccount =
                        it.accounts?.sortedByDescending { account -> account.isPrimary }
                    setUpDataAccount(sortedAccount)
                }
                is ViewState.Error -> {
                    showError(
                        getString(R.string.general_error_message)
                    )
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setUpAdapterAccount() {
        adapterAccount = AccountListAdapter(storage).apply {
            listener = this@DashboardActivity
        }

        val orientation: Int = resources.configuration.orientation
        val layoutManagerOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager.HORIZONTAL
        } else {
            LinearLayoutManager.VERTICAL
        }

        val snapHelper: SnapHelper = LinearSnapHelper()
        binding.recyclerViewAccount.apply {
            adapter = adapterAccount
            layoutManager = LinearLayoutManager(context, layoutManagerOrientation, false)
            snapHelper.attachToRecyclerView(this)
        }
    }

    override fun onClickAccount(accountId: Long?) {
        openAccountPage(accountId)
    }

    override fun onClickAddAccount() {
        openAccountPage(null)
    }

    private fun setUpAdapterTransaction() {
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

    private fun setUpDataOverview(overviewIncome: Double, overviewExpense: Double) {
        binding.textOverviewIncomeAmount?.text = overviewIncome.convertPriceWithCurrencyFormat(
            storage.getSymbolCurrency()
        )

        binding.textOverviewExpenseAmount?.text = overviewExpense.convertPriceWithCurrencyFormat(
            storage.getSymbolCurrency()
        )
    }

    private fun setUpDataAccount(accounts: List<Account>?) {
        accounts?.let {
            adapterAccount.updateAccounts(accounts)
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

    private fun openAccountPage(accountId: Long?) {
        accountResult.launch(accountRouter.openAccountPage(this, accountId))
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

}