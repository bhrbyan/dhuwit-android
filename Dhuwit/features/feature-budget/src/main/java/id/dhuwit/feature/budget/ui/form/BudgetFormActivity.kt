package id.dhuwit.feature.budget.ui.form

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.ListPopupWindow
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.budget.model.BudgetType
import id.dhuwit.core.extension.disabled
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFormActivityBinding
import id.dhuwit.feature.budget.ui.BudgetConstants.DEFAULT_BUDGET_ID
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_BUDGET_ID

@AndroidEntryPoint
class BudgetFormActivity : BaseActivity() {

    private var dropDownBudgetType: ListPopupWindow? = null
    private var dropDownBudgetPeriodDate: ListPopupWindow? = null
    private lateinit var binding: BudgetFormActivityBinding
    private val viewModel: BudgetFormViewModel by viewModels()

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
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {

            }
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
                add("First day of the month")
                for (date in 2..30) {
                    val dateString = when (date) {
                        2 -> "${date}nd"
                        3 -> "${date}rd"
                        else -> "${date}th"
                    }
                    add(dateString)
                }
                add("Last day of the month")
            }

            val adapter = ArrayAdapter(this@BudgetFormActivity, R.layout.budget_type_item, dates)
            setAdapter(adapter)

            setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                binding.inputTextBudgetType.setText(dates[position])
                dismiss()
            }

            // Set default budget type, not support weekly and yearly for now
            binding.inputTextBudgetPeriodDate.setText(dates[0])
            binding.inputTextBudgetPeriodDate.disabled()
        }
    }

    private fun setUpBudgetType() {
        dropDownBudgetType = ListPopupWindow(this, null, R.attr.listPopupWindowStyle).apply {
            anchorView = binding.inputTextBudgetType

            val items = listOf(
                BudgetType.Weekly.toString(),
                BudgetType.Monthly.toString(),
                BudgetType.Yearly.toString()
            )
            val adapter = ArrayAdapter(this@BudgetFormActivity, R.layout.budget_type_item, items)
            setAdapter(adapter)

            setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                binding.inputTextBudgetType.setText(items[position])
                dismiss()
            }

            // Set default budget type, not support weekly and yearly for now
            binding.inputTextBudgetType.setText(items[1])
            binding.inputTextBudgetType.disabled()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dropDownBudgetType = null
    }
}