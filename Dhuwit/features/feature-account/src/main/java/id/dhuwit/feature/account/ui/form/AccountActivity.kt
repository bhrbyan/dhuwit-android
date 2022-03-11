package id.dhuwit.feature.account.ui.form

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.*
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountActivityBinding
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class AccountActivity : BaseActivity() {

    private val viewModel: AccountViewModel by viewModels()

    private lateinit var binding: AccountActivityBinding

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = AccountActivityBinding.inflate(layoutInflater)
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
            viewState.observe(this@AccountActivity) {
                when (it) {
                    is AccountViewState.CreateAccount -> {
                        setUpViewCreateAccount()
                    }
                    is AccountViewState.GetAccount -> {
                        setUpViewUpdateAccount(it.account, it.accountsMoreThanOne)
                    }
                    is AccountViewState.Success -> {
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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setUpViewCreateAccount() {
        supportActionBar?.title = getString(R.string.account_form_create_toolbar_title)
        with(binding) {
            buttonSave.visible()
            switchPrimaryAccount.isChecked = false
        }
    }

    private fun setUpViewUpdateAccount(data: Account?, accountsMoreThanOne: Boolean) {
        supportActionBar?.title = getString(R.string.account_form_update_toolbar_title)
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