package id.dhuwit.feature.budget.ui.form.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPeriodType
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.enabled
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetFormSettingFragmentBinding
import id.dhuwit.feature.budget.ui.form.BudgetFormActivity
import id.dhuwit.state.ViewState

@AndroidEntryPoint
class BudgetFormSettingFragment : BaseFragment() {

    private var binding: BudgetFormSettingFragmentBinding? = null
    private var dropDownBudgetType: ListPopupWindow? = null
    private var dropDownBudgetPeriodDate: ListPopupWindow? = null

    private val viewModel: BudgetFormSettingViewModel by viewModels()
    private val periodDates: MutableList<String> = mutableListOf()
    private var periodTypes: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BudgetFormSettingFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun init() {
        setUpBudgetType()
        setUpBudgetPeriodDate()
    }

    override fun listener() {
        binding?.inputTextBudgetName?.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding?.buttonCreate?.disabled()
                binding?.buttonUpdate?.disabled()
            } else {
                binding?.buttonCreate?.enabled()
                binding?.buttonUpdate?.enabled()
            }
        }

        binding?.buttonCreate?.setOnClickListener {
            binding?.inputTextBudgetName?.text?.toString()?.let { name ->
                viewModel.createBudget(name)
            }
        }

        binding?.buttonUpdate?.setOnClickListener {

        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetFormSettingViewState.GetBudget -> {
                    if (it.budget != null) {
                        setUpViewUpdateBudget(it.budget)
                    } else {
                        setUpViewCreateBudget()
                    }
                }
                is BudgetFormSettingViewState.CreateBudget -> {
                    openBudgetPlanPage()
                }
                is ViewState.Error -> showError()
            }
        }
    }

    private fun openBudgetPlanPage() {
        (activity as BudgetFormActivity).showFormPlanPage()
    }

    private fun setUpViewCreateBudget() {
        binding?.inputTextBudgetType?.setText(BudgetPeriodType.Monthly.toString())
        binding?.inputTextBudgetPeriodDate?.setText(periodDates.first())

        binding?.buttonCreate?.visible()
        binding?.buttonUpdate?.gone()
    }

    private fun setUpViewUpdateBudget(budget: Budget) {
        binding?.inputTextBudgetName?.setText(budget.name)
        binding?.inputTextBudgetType?.setText(budget.periodType.toString())

        val periodDate = when (budget.periodDate) {
            1 -> getString(R.string.budget_form_setting_period_date_first)
            31 -> getString(R.string.budget_form_setting_period_date_last)
            else -> {
                when (budget.periodDate) {
                    2 -> "${budget.periodDate}nd"
                    3 -> "${budget.periodDate}rd"
                    else -> "${budget.periodDate}th"
                }
            }
        }
        binding?.inputTextBudgetPeriodDate?.setText(periodDate)

        binding?.buttonCreate?.gone()
        binding?.buttonUpdate?.visible()
    }

    private fun setUpBudgetPeriodDate() {
        dropDownBudgetPeriodDate =
            ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle).apply {
                anchorView = binding?.inputTextBudgetPeriodDate

                periodDates.apply {
                    for (date in 1..31) {
                        val dateString = when (date) {
                            1 -> getString(R.string.budget_form_setting_period_date_first)
                            2 -> "${date}nd"
                            3 -> "${date}rd"
                            31 -> getString(R.string.budget_form_setting_period_date_last)
                            else -> "${date}th"
                        }

                        add(dateString)
                    }
                }

                val adapter =
                    ArrayAdapter(requireContext(), R.layout.budget_period_item, periodDates)
                setAdapter(adapter)

                setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                    binding?.inputTextBudgetType?.setText(periodDates[position])
                    dismiss()
                }

                // Set default budget type, not support weekly and yearly for now
                binding?.inputTextBudgetPeriodDate?.disabled()
            }
    }

    private fun setUpBudgetType() {
        dropDownBudgetType =
            ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle).apply {
                anchorView = binding?.inputTextBudgetType

                periodTypes = mutableListOf(
                    BudgetPeriodType.Weekly.toString(),
                    BudgetPeriodType.Monthly.toString(),
                    BudgetPeriodType.Yearly.toString()
                )
                val adapter =
                    ArrayAdapter(requireContext(), R.layout.budget_period_item, periodTypes)
                setAdapter(adapter)

                setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                    binding?.inputTextBudgetType?.setText(periodTypes[position])
                    dismiss()
                }

                // Set default budget type, not support weekly and yearly for now
                binding?.inputTextBudgetType?.disabled()
            }
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
        const val KEY_BUDGET_ID: String = "budget_id"
        fun newInstance(budgetId: Long?): BudgetFormSettingFragment =
            BudgetFormSettingFragment().apply {
                arguments = Bundle().apply {
                    budgetId?.let {
                        putLong(KEY_BUDGET_ID, it)
                    }
                }
            }
    }
}