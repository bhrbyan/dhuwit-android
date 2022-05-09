package id.dhuwit.feature.budget.ui.form.select

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.enabled
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.budget.databinding.BudgetFormPlanSelectActivityBinding
import id.dhuwit.feature.category.router.CategoryRouter
import id.dhuwit.feature.category.ui.budget.CategoryBudgetListConstants
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetFormPlanSelectActivity : BaseActivity() {

    private lateinit var binding: BudgetFormPlanSelectActivityBinding
    private lateinit var budgetPlanType: BudgetPlanType

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

        budgetPlanType =
            BudgetPlanType.getBudgetPlanType(intent.getStringExtra("KEY_BUDGET_PLAN_TYPE"))
    }

    override fun listener() {
        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.inputTextCategory.setOnClickListener {
            openBudgetCategoryPage(budgetPlanType)
        }

        binding.inputTextAmount.apply {
            setCurrency(storage.getSymbolCurrency())
            setDecimals(false)
            setSeparator(SEPARATOR)
            addTextChangedListener {
                validationRequirement()
            }
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetFormPlanSelectViewState.SetSelectedCategory -> {
                    binding.inputTextCategory.setText(it.category.name)
                }
                is BudgetFormPlanSelectViewState.SetUpViewAddPlan -> {
                    setUpViewAddPlan()
                }
                is BudgetFormPlanSelectViewState.SetUpViewUpdatePlan -> {
                    setUpViewUpdatePlan()
                }
            }
        }
    }

    private fun setUpViewAddPlan() {
        binding.buttonSave.visible()
    }

    private fun setUpViewUpdatePlan() {
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

    companion object {
        private const val SEPARATOR: String = "."
    }
}