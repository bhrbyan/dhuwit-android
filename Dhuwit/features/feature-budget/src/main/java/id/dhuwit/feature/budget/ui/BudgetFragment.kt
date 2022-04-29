package id.dhuwit.feature.budget.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFragmentBinding
import id.dhuwit.state.ViewState

@AndroidEntryPoint
class BudgetFragment : BaseFragment() {

    private var binding: BudgetFragmentBinding? = null
    private val viewModel: BudgetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BudgetFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun init() {

    }

    override fun listener() {

    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetViewState.GetBudget -> {
                    binding?.groupEmptyState?.gone()
                }
                is BudgetViewState.ShowEmptyState -> {
                    binding?.groupEmptyState?.visible()
                }
                is ViewState.Error -> {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        binding?.let {
            Snackbar.make(
                it.root,
                getString(R.string.general_error_message),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}