package id.dhuwit.feature.budget.ui.plan

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetPlanActivityBinding
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_BUDGET_PLAN_TYPE
import id.dhuwit.feature.budget.ui.plan.adapter.BudgetPlanAdapter
import id.dhuwit.feature.budget.ui.plan.adapter.BudgetPlanListener
import id.dhuwit.feature.budget.ui.plan.dialog.BudgetPlanAmountDialogFragment
import id.dhuwit.feature.budget.ui.plan.dialog.BudgetPlanAmountListener
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetPlanActivity : BaseActivity(), BudgetPlanListener, BudgetPlanAmountListener {

    private lateinit var binding: BudgetPlanActivityBinding
    private lateinit var adapterPlan: BudgetPlanAdapter
    private val viewModel: BudgetPlanViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = BudgetPlanActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
    }

    override fun listener() {
        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.buttonAdd.setOnClickListener {
            viewModel.addBudgetPlan()
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetPlanViewState.GetBudgetPlans -> {
                    if (it.plans.isNullOrEmpty()) {
                        // show empty state, for now category can't be empty
                        // because no feature for delete category
                        setTotalAmount(0.0)
                    } else {
                        adapterPlan.updateList(it.plans)
                        val totalAmount = it.plans.sumOf { plan -> plan.amount ?: 0.0 }
                        setTotalAmount(totalAmount)
                    }
                }
                is BudgetPlanViewState.UpdateAmount -> {
                    adapterPlan.updateItem(it.categoryId, it.plans)
                    val totalAmount = it.plans.sumOf { plan -> plan.amount ?: 0.0 }
                    setTotalAmount(totalAmount)
                }
                is BudgetPlanViewState.SaveBudgetPlan -> {
                    setResult(RESULT_OK, Intent().apply {
                        Bundle().apply {
                            putExtra(KEY_BUDGET_PLAN_TYPE, it.budgetPlanType.toString())
                        }
                    })
                    finish()
                }
                is ViewState.Error -> {
                    showError()
                }
            }
        }
    }

    private fun setUpAdapter() {
        adapterPlan = BudgetPlanAdapter(storage).apply {
            listener = this@BudgetPlanActivity
        }

        binding.recyclerView.apply {
            adapter = adapterPlan
            layoutManager = LinearLayoutManager(this@BudgetPlanActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@BudgetPlanActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onClickItem(categoryId: Long, amount: Double?) {
        openDialogInputAmount(categoryId, amount)
    }

    private fun openDialogInputAmount(categoryId: Long, amount: Double?) {
        BudgetPlanAmountDialogFragment.newInstance(categoryId, amount)
            .show(supportFragmentManager, null)
    }

    override fun onClickAdd(categoryId: Long?, amount: Double?) {
        viewModel.updateAmount(categoryId, amount)
    }

    private fun setTotalAmount(amount: Double) {
        binding.textTotalAmountPlan.text =
            amount.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}