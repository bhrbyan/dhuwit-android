package id.dhuwit.feature.dashboard.ui.overview

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
import id.dhuwit.feature.dashboard.R
import id.dhuwit.feature.dashboard.adapter.DashboardTransactionHeaderAdapter
import id.dhuwit.feature.dashboard.adapter.DashboardTransactionItemListener
import id.dhuwit.feature.dashboard.databinding.DashboardOverviewFragmentBinding
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class DashboardOverviewFragment : BaseFragment(), DashboardTransactionItemListener {

    private lateinit var adapterTransactionHeader: DashboardTransactionHeaderAdapter

    private var binding: DashboardOverviewFragmentBinding? = null
    private val viewModelOverview: DashboardOverviewViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardOverviewFragmentBinding.inflate(
            inflater, container, false
        )
        return binding?.root
    }

    override fun init() {
        setUpAdapterTransaction()

        viewModelOverview.setDefaultPeriodDate()
        viewModelOverview.getOverview()
    }

    override fun listener() {
        binding?.let {
            it.imageNext.setOnClickListener {
                viewModelOverview.onNextPeriodDate()
            }

            it.imagePrevious.setOnClickListener {
                viewModelOverview.onPreviousPeriodDate()
            }
        }
    }

    override fun observer() {
        viewModelOverview.viewState.observe(this) {
            when (it) {
                is DashboardOverviewViewState.GetOverview -> {
                    setUpDataTransaction(it.dashboard.transactions)
                    setUpDataOverview(it.dashboard.overviewIncome, it.dashboard.overviewExpense)
                }
                is DashboardOverviewViewState.TransactionNotFound -> {
                    showError(
                        getString(R.string.dashboard_transactions_not_found)
                    )
                }
                is DashboardOverviewViewState.SetPeriodDate -> {
                    binding?.textMonth?.text = it.periodDate
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
        adapterTransactionHeader = DashboardTransactionHeaderAdapter().apply {
            listener = this@DashboardOverviewFragment
        }

        binding?.recyclerViewTransaction?.apply {
            adapter = adapterTransactionHeader
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClickTransaction(transaction: Transaction?) {
//        (activity as DashboardActivity).openTransactionPage(transaction?.id)
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
        viewModelOverview.getOverview()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}