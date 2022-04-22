package id.dhuwit.feature.overview.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionListType
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewTransactionFragmentBinding
import id.dhuwit.feature.overview.ui.overview.OverviewFragment
import id.dhuwit.feature.overview.ui.transaction.adapter.OverviewTransactionHeaderAdapter
import id.dhuwit.feature.overview.ui.transaction.adapter.OverviewTransactionItemListener
import id.dhuwit.feature.transaction.router.TransactionRouter
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class OverviewTransactionFragment : BaseFragment(), OverviewTransactionItemListener {

    private lateinit var adapterTransactionHeader: OverviewTransactionHeaderAdapter

    private var binding: OverviewTransactionFragmentBinding? = null
    private val viewModel: OverviewTransactionViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var transactionRouter: TransactionRouter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OverviewTransactionFragmentBinding.inflate(
            inflater, container, false
        )
        return binding?.root
    }

    override fun init() {
        setUpAdapterTransaction()

        viewModel.setDefaultPeriodDate()
        viewModel.getOverview()
    }

    override fun listener() {
        binding?.let {
            it.imageNext.setOnClickListener {
                viewModel.onNextPeriodDate()
            }

            it.imagePrevious.setOnClickListener {
                viewModel.onPreviousPeriodDate()
            }

            it.cardOverviewIncome.setOnClickListener {
                viewModel.openTransactionListPage(TransactionType.Income)
            }

            it.cardOverviewExpense.setOnClickListener {
                viewModel.openTransactionListPage(TransactionType.Expense)
            }
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is OverviewTransactionViewState.GetOverview -> {
                    setUpDataTransaction(it.overviewTransaction.transactions)
                    setUpDataOverview(
                        it.overviewTransaction.totalIncomeTransaction,
                        it.overviewTransaction.totalExpenseTransaction
                    )
                }
                is OverviewTransactionViewState.TransactionNotFound -> {
                    showError(
                        getString(R.string.overview_transactions_not_found)
                    )
                }
                is OverviewTransactionViewState.SetPeriodDate -> {
                    binding?.textMonth?.text = it.periodDate
                }
                is OverviewTransactionViewState.OpenTransactionListPage -> {
                    val transactionListType = when (it.transactionType) {
                        is TransactionType.Income -> TransactionListType.Income
                        is TransactionType.Expense -> TransactionListType.Expense
                    }
                    startActivity(
                        transactionRouter.openTransactionListPage(
                            context = requireContext(),
                            periodDate = it.periodDate,
                            transactionListType = transactionListType,
                            transactionType = it.transactionType
                        )
                    )
                }
                is ViewState.Error -> {
                    showError(
                        getString(R.string.general_error_message)
                    )
                }
            }
        }
    }

    private fun setUpAdapterTransaction() {
        adapterTransactionHeader = OverviewTransactionHeaderAdapter().apply {
            listener = this@OverviewTransactionFragment
        }

        binding?.recyclerViewTransaction?.apply {
            adapter = adapterTransactionHeader
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClickTransaction(transaction: Transaction?) {
        (parentFragment as OverviewFragment).openTransactionPage(transaction?.id)
    }

    private fun setUpDataTransaction(transactions: List<Transaction>?) {
        if (transactions.isNullOrEmpty()) {
            showMessageEmptyTransaction()
        } else {
            showTransaction()
            adapterTransactionHeader.updateList(transactions, storage.getSymbolCurrency())
        }
    }

    private fun showTransaction() {
        binding?.let {
            it.recyclerViewTransaction.visible()
            it.textEmptyTransaction.gone()
        }
    }

    private fun showMessageEmptyTransaction() {
        binding?.let {
            it.recyclerViewTransaction.gone()
            it.textEmptyTransaction.visible()
        }
    }

    private fun setUpDataOverview(overviewIncome: Double, overviewExpense: Double) {
        binding?.textOverviewIncomeAmount?.text = overviewIncome.convertPriceWithCurrencyFormat(
            storage.getSymbolCurrency()
        )

        binding?.textOverviewExpenseAmount?.text = overviewExpense.convertPriceWithCurrencyFormat(
            storage.getSymbolCurrency()
        )
    }

    private fun showError(message: String) {
        binding?.root?.let { view ->
            Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    fun updateDataOverview() {
        viewModel.getOverview()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}