package id.dhuwit.feature.budget.ui

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFragmentBinding
import id.dhuwit.feature.budget.ui.form.BudgetFormActivity
import id.dhuwit.feature.budget.ui.form.select.BudgetFormPlanSelectActivity
import id.dhuwit.state.ViewState

@AndroidEntryPoint
class BudgetFragment : BaseFragment() {

    private var binding: BudgetFragmentBinding? = null
    private val viewModel: BudgetViewModel by viewModels()

    private val budgetFormResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK || result.resultCode == RESULT_CANCELED) {
                viewModel.getBudgets()
            }
        }

    private val budgetPlanResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Update data
            }
        }

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
        binding?.let {
            it.buttonCreateBudget.setOnClickListener {
                viewModel.openFormBudget()
            }

            it.imageAddIncomes.setOnClickListener {
                openFormAddBudgetPlan(BudgetPlanType.Income)
            }

            it.imageAddExpenses.setOnClickListener {
                openFormAddBudgetPlan(BudgetPlanType.Expense)
            }

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
                is BudgetViewState.GetBudget -> {
                    if (it.budgets.isNullOrEmpty()) {
                        binding?.groupData?.gone()
                        binding?.groupEmptyState?.visible()
                    } else {
                        binding?.groupData?.visible()
                        binding?.groupEmptyState?.gone()

                        setDataBudget(it.budgets.first())
                        setDataIncomes(it.budgetPlanIncomes)
                        setDataExpenses(it.budgetPlanExpens)
                    }
                }
                is BudgetViewState.OpenFormBudget -> {
                    openFormBudget(it.budgetId)
                    viewModel.resetViewState()
                }
                is ViewState.Error -> {
                    showError()
                }
            }
        }
    }

    private fun setDataIncomes(budgetPlanIncomes: List<BudgetPlan>?) {
        if (budgetPlanIncomes.isNullOrEmpty()) {
            binding?.recyclerViewIncomes?.gone()
            binding?.textIncomesEmpty?.visible()
        } else {
            binding?.recyclerViewIncomes?.visible()
            binding?.textIncomesEmpty?.gone()
        }
    }

    private fun setDataExpenses(budgetPlanExpens: List<BudgetPlan>?) {
        if (budgetPlanExpens.isNullOrEmpty()) {
            binding?.recyclerViewExpenses?.gone()
            binding?.textExpensesEmpty?.visible()
        } else {
            binding?.recyclerViewExpenses?.visible()
            binding?.textExpensesEmpty?.gone()
        }
    }

    private fun setDataBudget(budget: Budget?) {
        binding?.textBudgetName?.text = budget?.name
    }

    private fun openFormBudget(budgetId: Long?) {
        budgetFormResult.launch(
            Intent(context, BudgetFormActivity::class.java).apply {
                budgetId?.let {
                    putExtra(BudgetConstants.KEY_BUDGET_ID, it)
                }
            }
        )
    }

    private fun openFormAddBudgetPlan(
        budgetPlanType: BudgetPlanType,
        categoryId: Long? = null,
        categoryAmount: Double = 0.0
    ) {
        budgetPlanResult.launch(
            Intent(context, BudgetFormPlanSelectActivity::class.java).apply {
                putExtra("KEY_BUDGET_PLAN_TYPE", budgetPlanType.toString())
                categoryId?.let {
                    putExtra("KEY_CATEGORY_ID", categoryId)
                    putExtra("KEY_CATEGORY_AMOUNT", categoryAmount)
                }
            }
        )
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