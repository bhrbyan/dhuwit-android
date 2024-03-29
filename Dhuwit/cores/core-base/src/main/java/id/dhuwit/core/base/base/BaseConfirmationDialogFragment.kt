package id.dhuwit.core.base.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.dhuwit.core.base.R
import id.dhuwit.core.base.databinding.BaseConfirmationDialogFragmentBinding


abstract class BaseConfirmationDialogFragment : DialogFragment() {

    protected abstract fun init()

    protected var binding: BaseConfirmationDialogFragmentBinding? = null

    protected open fun initStyle() {
        setStyle(STYLE_NO_TITLE, R.style.Uikit_Style_Dialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            setOnKeyListener { _, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BaseConfirmationDialogFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

}