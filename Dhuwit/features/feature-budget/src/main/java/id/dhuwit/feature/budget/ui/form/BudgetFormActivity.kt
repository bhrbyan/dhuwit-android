package id.dhuwit.feature.budget.ui.form

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFormActivityBinding
import id.dhuwit.feature.budget.ui.form.plan.BudgetFormPlanFragment
import id.dhuwit.feature.budget.ui.form.setting.BudgetFormSettingFragment
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetFormActivity : BaseActivity() {

    private lateinit var binding: BudgetFormActivityBinding

    private val viewModel: BudgetFormViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = BudgetFormActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listener() {
        binding.imageClose.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetFormViewState.SetUpViewFormBudget -> {
                    showFormSettingPage(it.budgetId)
                }
            }
        }
    }

    private fun showFormSettingPage(budgetId: Long? = null) {
        if (budgetId != null) {
            binding.textTitle.text = getString(R.string.budget_form_setting_update_title)
            binding.textDescription.text =
                getString(R.string.budget_form_setting_update_description)
        } else {
            binding.textTitle.text = getString(R.string.budget_form_setting_create_title)
            binding.textDescription.text =
                getString(R.string.budget_form_setting_create_description)
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, BudgetFormSettingFragment.newInstance(budgetId))
            .commit()
    }

    fun showFormPlanPage(
        budgetPlanType: BudgetPlanType = BudgetPlanType.Income
    ) {
        when (budgetPlanType) {
            is BudgetPlanType.Income -> {
                binding.textTitle.text = getString(R.string.budget_form_plan_income_title)
                binding.textDescription.text =
                    getString(R.string.budget_form_plan_income_description)
            }
            is BudgetPlanType.Expense -> {
                binding.textTitle.text = getString(R.string.budget_form_plan_expense_title)
                binding.textDescription.text =
                    getString(R.string.budget_form_plan_expense_description)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(
                binding.frameLayout.id,
                BudgetFormPlanFragment.newInstance(budgetPlanType)
            )
            .commit()
    }
}