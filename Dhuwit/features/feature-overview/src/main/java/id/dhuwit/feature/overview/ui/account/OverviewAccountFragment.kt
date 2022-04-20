package id.dhuwit.feature.overview.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.transaction.model.TransactionAccount
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewAccountFragmentBinding
import id.dhuwit.feature.overview.ui.account.adapter.OverviewAccountAdapter
import id.dhuwit.feature.overview.ui.account.adapter.OverviewAccountListener
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class OverviewAccountFragment : BaseFragment(), OverviewAccountListener {

    private var binding: OverviewAccountFragmentBinding? = null
    private val viewModel: OverviewAccountViewModel by viewModels()

    private lateinit var adapterAccount: OverviewAccountAdapter

    @Inject
    lateinit var storage: Storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OverviewAccountFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun init() {
        setUpAdapter()
    }

    override fun listener() {
        binding?.let {
            it.imageNext.setOnClickListener {
                viewModel.onNextPeriodDate()
            }

            it.imagePrevious.setOnClickListener {
                viewModel.onPreviousPeriodDate()
            }
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is OverviewAccountViewState.GetDetails -> {
                    setUpPeriodDate(it.periodDate)

                    if (it.accountTransaction.isNullOrEmpty()) {
                        showMessageEmptyCategory()
                    } else {
                        showList(it.accountTransaction)
                    }
                }
                is OverviewAccountViewState.CategoryNotFound -> {
                    showError(
                        getString(R.string.overview_categories_not_found)
                    )
                }
                is OverviewAccountViewState.SetPeriodDate -> {
                    binding?.textMonth?.text = it.periodDate
                }
                is ViewState.Error -> showError(
                    getString(R.string.general_error_message)
                )
            }
        }
    }

    private fun setUpPeriodDate(periodDate: String?) {
        binding?.textMonth?.text = periodDate
    }

    private fun showMessageEmptyCategory() {
        binding?.let {
            it.recyclerView.gone()
            it.textEmptyTransaction.visible()
        }
    }

    private fun showList(accountTransaction: List<TransactionAccount>) {
        binding?.let {
            it.recyclerView.visible()
            it.textEmptyTransaction.gone()

            adapterAccount.updateList(accountTransaction)
        }
    }

    private fun setUpAdapter() {
        adapterAccount = OverviewAccountAdapter(storage.getSymbolCurrency())
        binding?.recyclerView?.apply {
            adapter = adapterAccount
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onClickAccount(item: TransactionAccount) {
        Toast.makeText(requireContext(), item.accountName, Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}