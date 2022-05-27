package id.dhuwit.feature.account.ui.form

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.*
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountFormActivityBinding
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import id.dhuwit.uikit.databinding.ToolbarBinding
import javax.inject.Inject

@AndroidEntryPoint
class AccountFormActivity : BaseActivity() {

    private lateinit var binding: AccountFormActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding

    private val viewModel: AccountFormViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = AccountFormActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        setContentView(binding.root)

        setUpToolbar()
    }

    override fun listener() {
        with(binding) {
            inputTextAccountName.apply {
                addTextChangedListener {
                    validationRequirement()
                }
            }

            inputTextAccountBalance.apply {
                setCurrency(storage.getSymbolCurrency())
                setDecimals(false)
                setSeparator(SEPARATOR)
                addTextChangedListener {
                    validationRequirement()
                }
            }

            buttonSave.setOnClickListener {
                with(binding) {
                    viewModel.createAccount(
                        name = inputTextAccountName.text.toString(),
                        balance = inputTextAccountBalance.cleanDoubleValue,
                        isPrimary = switchPrimaryAccount.isChecked
                    )
                }
            }

            buttonUpdate.setOnClickListener {
                viewModel.updateAccount(
                    name = inputTextAccountName.text.toString(),
                    balance = inputTextAccountBalance.cleanDoubleValue,
                    isPrimary = switchPrimaryAccount.isChecked
                )
            }

            buttonDelete.setOnClickListener {
                viewModel.deleteAccount()
            }
        }
    }

    override fun observer() {
        with(viewModel) {
            viewState.observe(this@AccountFormActivity) {
                when (it) {
                    is AccountFormViewState.CreateAccount -> {
                        setUpViewCreateAccount()
                    }
                    is AccountFormViewState.GetAccount -> {
                        setUpViewUpdateAccount(it.account, it.accountsMoreThanOne)
                    }
                    is AccountFormViewState.Success -> {
                        closePage()
                    }
                    is ViewState.Error -> {
                        showError()
                    }
                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    private fun setUpToolbar() {
        bindingToolbar.apply {
            imageActionLeft.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close))
                setOnClickListener {
                    finish()
                }
                visible()
            }
        }
    }

    private fun setUpViewCreateAccount() {
        bindingToolbar.textTitle.text = getString(R.string.account_form_create_toolbar_title)
        with(binding) {
            buttonSave.visible()
            switchPrimaryAccount.isChecked = false
        }
    }

    private fun setUpViewUpdateAccount(data: Account?, accountsMoreThanOne: Boolean) {
        bindingToolbar.textTitle.text = getString(R.string.account_form_update_toolbar_title)
        with(binding) {
            buttonUpdate.visible()
            if (accountsMoreThanOne) {
                buttonDelete.visible()
            } else {
                buttonDelete.gone()
            }

            inputTextAccountName.setText(data?.name)
            inputTextAccountBalance.setText(data?.balance?.convertDoubleToString())
            switchPrimaryAccount.isChecked = data?.isPrimary ?: false
        }
    }

    private fun validationRequirement() {
        with(binding) {
            if (inputTextAccountName.text.isNullOrEmpty() ||
                inputTextAccountBalance.text.isNullOrEmpty() ||
                inputTextAccountBalance.cleanDoubleValue == 0.0
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

    private fun closePage() {
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        private const val SEPARATOR: String = "."
    }
}