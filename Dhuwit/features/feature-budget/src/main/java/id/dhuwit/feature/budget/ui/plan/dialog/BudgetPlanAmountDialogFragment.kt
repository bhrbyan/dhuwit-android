package id.dhuwit.feature.budget.ui.plan.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseDialogFragment
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.budget.databinding.BudgetPlanAmountDialogFragmentBinding
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_BUDGET_PLAN_AMOUNT
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_CATEGORY_ID
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetPlanAmountDialogFragment : BaseDialogFragment() {

    private var binding: BudgetPlanAmountDialogFragmentBinding? = null

    private var listener: BudgetPlanAmountListener? = null

    @Inject
    lateinit var storage: Storage

    override fun onAttach(context: Context) {
        initStyle()
        super.onAttach(context)
        listener = when {
            context is BudgetPlanAmountListener -> context
            parentFragment is BudgetPlanAmountListener -> parentFragment as BudgetPlanAmountListener
            else -> throw Exception("$activity or $parentFragment must implement BudgetPlanAmountListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BudgetPlanAmountDialogFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun init() {
        val categoryId = arguments?.getLong(KEY_CATEGORY_ID)
        val amount = arguments?.getDouble(KEY_BUDGET_PLAN_AMOUNT)

        binding?.inputTextAmount?.let {
            it.setCurrency(storage.getSymbolCurrency())
            it.setDecimals(false)
            it.setSeparator(SEPARATOR)
        }

        if (amount != 0.0) {
            binding?.inputTextAmount?.setText(amount?.convertPriceWithCurrencyFormat(storage.getSymbolCurrency()))
        }

        binding?.buttonAdd?.setOnClickListener {
            listener?.onClickAdd(categoryId, binding?.inputTextAmount?.cleanDoubleValue)
            dismiss()
        }

        binding?.buttonCancel?.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val SEPARATOR: String = "."

        fun newInstance(categoryId: Long, amount: Double?): BudgetPlanAmountDialogFragment {
            return BudgetPlanAmountDialogFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_CATEGORY_ID, categoryId)
                    putDouble(KEY_BUDGET_PLAN_AMOUNT, amount ?: 0.0)
                }
            }
        }
    }
}