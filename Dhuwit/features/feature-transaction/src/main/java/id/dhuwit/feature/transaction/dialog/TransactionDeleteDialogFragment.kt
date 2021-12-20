package id.dhuwit.feature.transaction.dialog

import android.content.Context
import id.dhuwit.core.base.BaseConfirmationDialogFragment
import id.dhuwit.feature.transaction.R

class TransactionDeleteDialogFragment : BaseConfirmationDialogFragment() {

    private var listener: TransactionDeleteConfirmationListener? = null

    override fun onAttach(context: Context) {
        initStyle()
        super.onAttach(context)
        listener = when {
            context is TransactionDeleteConfirmationListener -> context
            parentFragment is TransactionDeleteConfirmationListener -> parentFragment as TransactionDeleteConfirmationListener
            else -> throw Exception("$activity or $parentFragment must implement TransactionDeleteConfirmationListener")
        }
    }

    override fun init() {
        with(binding) {
            this?.textTitle?.text = getString(R.string.transaction_delete_title)
            this?.textMessage?.text = getString(R.string.transaction_delete_message)
            this?.buttonPositive?.apply {
                text = getString(R.string.transaction_delete_button_positive)
                setOnClickListener {
                    listener?.onConfirmDeleteTransaction()
                    dismiss()
                }
            }
            this?.buttonNegative?.apply {
                text = getString(R.string.transaction_delete_button_negative)
                setOnClickListener {
                    dismiss()
                }
            }
        }
    }
}