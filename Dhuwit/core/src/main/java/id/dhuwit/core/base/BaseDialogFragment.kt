package id.dhuwit.core.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.DialogFragment
import id.dhuwit.core.R


abstract class BaseDialogFragment : DialogFragment() {

    protected abstract fun init()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

}