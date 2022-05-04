package id.dhuwit.feature.budget.ui.form

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.ListPopupWindow
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPeriodType
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFormActivityBinding
import id.dhuwit.feature.budget.ui.BudgetConstants.DEFAULT_BUDGET_ID
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_BUDGET_ID
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_BUDGET_PLAN_TYPE
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_CATEGORY_TYPE
import id.dhuwit.feature.budget.ui.plan.BudgetPlanActivity
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetFormActivity : BaseActivity() {

    private lateinit var binding: BudgetFormActivityBinding

    private val viewModel: BudgetFormViewModel by viewModels()

    private var dropDownBudgetType: ListPopupWindow? = null
    private var dropDownBudgetPeriodDate: ListPopupWindow? = null

    private val budgetPlanResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null) {
                val budgetPlanType: BudgetPlanType = BudgetPlanType.getBudgetPlanType(
                    result.data?.getStringExtra(KEY_BUDGET_PLAN_TYPE)
                )

                viewModel.updatePlan(budgetPlanType)
            }
        }

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = BudgetFormActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val budgetId: Long = intent.getLongExtra(KEY_BUDGET_ID, DEFAULT_BUDGET_ID)
        setUpToolbarTitle(budgetId)
        setUpBudgetType()
        setUpBudgetPeriodDate()
    }

    override fun listener() {
        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.inputTextBudgetType.setOnClickListener {
            dropDownBudgetType?.show()
        }

        binding.inputTextBudgetPeriodDate.setOnClickListener {
            dropDownBudgetPeriodDate?.show()
        }

        binding.layoutIncome.setOnClickListener {
            openBudgetPlanPage(BudgetPlanType.Income)
        }

        binding.layoutExpense.setOnClickListener {
            openBudgetPlanPage(BudgetPlanType.Expense)
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetFormViewState.GetBudget -> {
                    setUpData(it.budget)
                }
                is BudgetFormViewState.UpdatePlan -> {
                    when (it.budgetPlanType) {
                        is BudgetPlanType.Income -> {
                            setTextBudgetPlanIncomes(it.budgetPlans)
                        }
                        is BudgetPlanType.Expense -> {
                            setTextBudgetPlanExpenses(it.budgetPlans)
                        }
                    }
                }
                is ViewState.Error -> showError()
            }
        }
    }

    private fun setUpData(budget: Budget?) {
        binding.inputTextBudgetName.setText(budget?.name)
        binding.inputTextBudgetType.setText(budget?.setting?.periodType?.toString())

        val periodDate = when (budget?.setting?.periodDate) {
            1 -> getString(R.string.budget_form_period_date_first)
            31 -> getString(R.string.budget_form_period_date_last)
            else -> {
                when (budget?.setting?.periodDate) {
                    2 -> "${budget.setting?.periodDate}nd"
                    3 -> "${budget.setting?.periodDate}rd"
                    else -> "${budget?.setting?.periodDate}th"
                }
            }
        }
        binding.inputTextBudgetPeriodDate.setText(periodDate)
        setTextBudgetPlanIncomes(budget?.incomes)
        setTextBudgetPlanExpenses(budget?.expenses)
    }

    private fun setTextBudgetPlanIncomes(budgetPlans: List<BudgetPlan>?) {
        if (budgetPlans.isNullOrEmpty()) {
            binding.textPlanIncomeTotal.text =
                getString(R.string.budget_form_hint_plan_income_empty)
            binding.textPlanIncomeAmount.gone()
        } else {
            binding.textPlanIncomeTotal.text =
                getString(R.string.budget_form_hint_plan_income_total, budgetPlans.size)

            binding.textPlanIncomeAmount.visible()
            binding.textPlanIncomeAmount.text = budgetPlans.sumOf { it.amount ?: 0.0 }
                .convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
        }
    }

    private fun setTextBudgetPlanExpenses(budgetPlans: List<BudgetPlan>?) {
        if (budgetPlans.isNullOrEmpty()) {
            binding.textPlanExpenseTotal.text =
                getString(R.string.budget_form_hint_plan_expense_empty)
            binding.textPlanExpenseAmount.gone()
        } else {
            binding.textPlanExpenseTotal.text =
                getString(R.string.budget_form_hint_plan_expense_total, budgetPlans.size)

            binding.textPlanExpenseAmount.visible()
            binding.textPlanExpenseAmount.text = budgetPlans.sumOf { it.amount ?: 0.0 }
                .convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
        }
    }

    private fun setUpToolbarTitle(budgetId: Long) {
        binding.textTitle.text = if (budgetId == DEFAULT_BUDGET_ID) {
            getString(R.string.budget_form_title_add)
        } else {
            getString(R.string.budget_form_title_edit)
        }
    }

    private fun setUpBudgetPeriodDate() {
        dropDownBudgetPeriodDate = ListPopupWindow(this, null, R.attr.listPopupWindowStyle).apply {
            anchorView = binding.inputTextBudgetPeriodDate

            val dates = ArrayList<String>()
            dates.apply {
                add(getString(R.string.budget_form_period_date_first))
                for (date in 2..30) {
                    val dateString = when (date) {
                        2 -> "${date}nd"
                        3 -> "${date}rd"
                        else -> "${date}th"
                    }
                    add(dateString)
                }
                add(getString(R.string.budget_form_period_date_last))
            }

            val adapter = ArrayAdapter(this@BudgetFormActivity, R.layout.budget_type_item, dates)
            setAdapter(adapter)

            setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                binding.inputTextBudgetType.setText(dates[position])
                dismiss()
            }

            // Set default budget type, not support weekly and yearly for now
            binding.inputTextBudgetPeriodDate.disabled()
        }
    }

    private fun setUpBudgetType() {
        dropDownBudgetType = ListPopupWindow(this, null, R.attr.listPopupWindowStyle).apply {
            anchorView = binding.inputTextBudgetType

            val items = listOf(
                BudgetPeriodType.Weekly.toString(),
                BudgetPeriodType.Monthly.toString(),
                BudgetPeriodType.Yearly.toString()
            )
            val adapter = ArrayAdapter(this@BudgetFormActivity, R.layout.budget_type_item, items)
            setAdapter(adapter)

            setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                binding.inputTextBudgetType.setText(items[position])
                dismiss()
            }

            // Set default budget type, not support weekly and yearly for now
            binding.inputTextBudgetType.disabled()
        }
    }

    private fun openBudgetPlanPage(planType: BudgetPlanType) {
        budgetPlanResult.launch(
            Intent(this, BudgetPlanActivity::class.java).apply {
                putExtra(KEY_CATEGORY_TYPE, planType.toString())
            }
        )
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}