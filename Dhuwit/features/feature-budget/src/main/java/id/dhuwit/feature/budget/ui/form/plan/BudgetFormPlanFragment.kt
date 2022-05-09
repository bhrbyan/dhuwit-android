package id.dhuwit.feature.budget.ui.form.plan

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.extension.*
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFormPlanFragmentBinding
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_SELECT_BUDGET_ID
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_SELECT_BUDGET_PLAN_TYPE
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_SELECT_CATEGORY_ID
import id.dhuwit.feature.budget.ui.form.BudgetFormActivity
import id.dhuwit.feature.budget.ui.form.plan.adapter.BudgetFormPlanAdapter
import id.dhuwit.feature.budget.ui.form.plan.adapter.BudgetFormPlanListener
import id.dhuwit.feature.budget.ui.form.select.BudgetFormPlanSelectActivity
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetFormPlanFragment : BaseFragment(), BudgetFormPlanListener {

    private var binding: BudgetFormPlanFragmentBinding? = null
    private val viewModel: BudgetFormPlanViewModel by viewModels()

    private lateinit var adapterPlan: BudgetFormPlanAdapter

    private var budgetPlanType: BudgetPlanType = BudgetPlanType.Income

    @Inject
    lateinit var storage: Storage

    private val selectPlanResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

            }

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BudgetFormPlanFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun init() {
        setUpAdapter()

        budgetPlanType =
            BudgetPlanType.getBudgetPlanType(arguments?.getString(KEY_BUDGET_PLAN_TYPE))
    }

    override fun listener() {
        binding?.buttonAdd?.setOnClickListener {
            openBudgetFormPlanSelect(budgetPlanType)
        }

        binding?.buttonNext?.setOnClickListener {
            (activity as BudgetFormActivity).showFormPlanPage(budgetPlanType = BudgetPlanType.Expense)
        }
    }

    private fun openBudgetFormPlanSelect(
        budgetPlanType: BudgetPlanType?,
        budgetId: Long? = null,
        categoryId: Long? = null
    ) {
        selectPlanResult.launch(
            Intent(requireContext(), BudgetFormPlanSelectActivity::class.java).apply {
                putExtra(KEY_SELECT_BUDGET_PLAN_TYPE, budgetPlanType.toString())
                putExtra(KEY_SELECT_BUDGET_ID, budgetId)
                putExtra(KEY_SELECT_CATEGORY_ID, categoryId)
            }
        )
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetFormPlanViewState.SetUpViewPlans -> {
                    if (it.plans.isNullOrEmpty()) {
                        binding?.textTotalIncome?.text =
                            DEFAULT_TOTAL_PLAN.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
                        binding?.recyclerView?.gone()
                        binding?.textEmptyPlans?.visible()
                        binding?.buttonNext?.disabled()
                    } else {
                        adapterPlan.updateList(it.plans)
                        binding?.textTotalIncome?.text = it.plans.sumOf { it.budgetAmount }
                            .convertPriceWithCurrencyFormat(storage.getSymbolCurrency())

                        binding?.recyclerView?.visible()
                        binding?.textEmptyPlans?.gone()
                        binding?.buttonNext?.enabled()
                    }
                }
                is ViewState.Error -> showError()
            }
        }
    }

    private fun setUpAdapter() {
        adapterPlan = BudgetFormPlanAdapter(storage).apply {
            listener = this@BudgetFormPlanFragment
        }

        binding?.recyclerView?.apply {
            adapter = adapterPlan
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onClickItemPlan(plan: BudgetPlan?) {
        openBudgetFormPlanSelect(plan?.budgetPlanType, plan?.budgetId, plan?.category?.id)
    }

    private fun showError() {
        binding?.root?.let {
            Snackbar.make(
                it,
                getString(R.string.general_error_message),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val DEFAULT_TOTAL_PLAN: Double = 0.0

        const val KEY_BUDGET_ID: String = "key_budget_id"
        const val KEY_BUDGET_PLAN_TYPE: String = "key_budget_plan_type"

        fun newInstance(
            budgetId: Long? = null,
            budgetPlanType: BudgetPlanType? = null
        ): BudgetFormPlanFragment {
            return BudgetFormPlanFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUDGET_PLAN_TYPE, budgetPlanType.toString())
                    budgetId?.let {
                        putLong(KEY_BUDGET_ID, budgetId)
                    }
                }
            }
        }
    }
}