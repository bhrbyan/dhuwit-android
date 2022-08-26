package id.dhuwit.core.base.base

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun init()
    abstract fun listener()
    abstract fun observer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        listener()
        observer()

        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}