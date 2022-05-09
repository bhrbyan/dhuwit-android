package id.dhuwit.feature.budget.ui.form.select

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.enabled
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFormPlanSelectActivityBinding
import id.dhuwit.feature.category.router.CategoryRouter
import id.dhuwit.feature.category.ui.budget.CategoryBudgetListConstants
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetFormPlanSelectActivity : BaseActivity() {

    private lateinit var binding: BudgetFormPlanSelectActivityBinding

    private val viewModel: BudgetFormPlanSelectViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var categoryRouter: CategoryRouter

    private val selectCategoryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val categoryId = result.data?.getLongExtra(
                    CategoryBudgetListConstants.KEY_SELECT_CATEGORY_ID,
                    -1
                ) ?: 0L
                val categoryName =
                    result.data?.getStringExtra(CategoryBudgetListConstants.KEY_SELECT_CATEGORY_NAME)
                        ?: ""
                val categoryType = CategoryType.getCategoryType(
                    result.data?.getStringExtra(
                        CategoryBudgetListConstants.KEY_SELECT_CATEGORY_TYPE
                    )
                )

                viewModel.setSelectedCategory(
                    Category(
                        id = categoryId,
                        name = categoryName,
                        type = categoryType
                    )
                )
            }
        }

    override fun init() {
        binding = BudgetFormPlanSelectActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listener() {
        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.inputTextCategory.setOnClickListener {
            viewModel.onOpenBudgetCategoryPage()
        }

        binding.inputTextAmount.apply {
            setCurrency(storage.getSymbolCurrency())
            setDecimals(false)
            setSeparator(SEPARATOR)
            addTextChangedListener {
                validationRequirement()
            }
        }

        binding.inputTextCategory.addTextChangedListener {
            validationRequirement()
        }

        binding.buttonSave.setOnClickListener {
            viewModel.saveBudgetPlan(
                binding.inputTextAmount.cleanDoubleValue
            )
        }

        binding.buttonUpdate.setOnClickListener {
            viewModel.updateBudgetPlan(
                binding.inputTextAmount.cleanDoubleValue
            )
        }

        binding.buttonDelete.setOnClickListener {
            viewModel.deleteBudgetPlan()
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetFormPlanSelectViewState.OpenBudgetCategoryPage -> {
                    openBudgetCategoryPage(it.budgetPlanType)
                }
                is BudgetFormPlanSelectViewState.SelectCategory -> {
                    binding.inputTextCategory.setText(it.category.name)
                }
                is BudgetFormPlanSelectViewState.SaveBudget -> {
                    setResult(RESULT_OK)
                    finish()
                }
                is BudgetFormPlanSelectViewState.UpdateBudget -> {
                    setResult(RESULT_OK)
                    finish()
                }
                is BudgetFormPlanSelectViewState.DeleteBudget -> {
                    setResult(RESULT_OK)
                    finish()
                }
                is BudgetFormPlanSelectViewState.GetBudgetPlan -> {
                    if (it.budgetPlan != null) {
                        setUpViewUpdatePlan(it.budgetPlan)
                    } else {
                        setUpViewAddPlan()
                    }
                }
                is ViewState.Error -> {
                    showError()
                }
            }
        }
    }

    private fun setUpViewAddPlan() {
        binding.buttonSave.visible()
        binding.buttonSave.disabled()
    }

    private fun setUpViewUpdatePlan(budgetPlan: BudgetPlan?) {
        binding.inputTextCategory.setText(budgetPlan?.category?.name)
        binding.inputTextAmount.setText(
            budgetPlan?.budgetAmount?.convertPriceWithCurrencyFormat(
                storage.getSymbolCurrency()
            )
        )

        binding.buttonDelete.visible()
        binding.buttonUpdate.visible()
    }

    private fun openBudgetCategoryPage(budgetPlanType: BudgetPlanType) {
        val categoryType: CategoryType = if (budgetPlanType is BudgetPlanType.Income) {
            CategoryType.Income
        } else {
            CategoryType.Expense
        }

        selectCategoryResult.launch(
            categoryRouter.openCategoryBudgetListPage(this, categoryType.toString())
        )
    }

    private fun validationRequirement() {
        with(binding) {
            if (inputTextCategory.text.isNullOrEmpty() ||
                inputTextAmount.text.isNullOrEmpty() ||
                inputTextAmount.cleanDoubleValue == 0.0
            ) {
                buttonSave.disabled()
                buttonUpdate.disabled()
            } else {
                buttonSave.enabled()
                buttonUpdate.enabled()
            }
        }
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val SEPARATOR: String = "."
    }
}