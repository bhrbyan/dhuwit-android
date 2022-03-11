package id.dhuwit.feature.account.ui.form

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertDoubleToString
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.enabled
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountActivityBinding
import id.dhuwit.state.State
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
        setUpInputTextBalance()
        with(binding) {
            buttonSave.setOnClickListener {
                viewModel.createAccount()
            }

            buttonUpdate.setOnClickListener {
                viewModel.updateAccount()
            }

            buttonDelete.setOnClickListener {
                viewModel.deleteAccount()
            }

            inputTextAccountName.apply {
                addTextChangedListener {
                    viewModel.setAccountName(it.toString())
                    viewModel.checkInputField()
                }
            }

            switchPrimaryAccount.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    viewModel.setStatusPrimaryAccount(isChecked)
                }
            }
        }
    }

    override fun observer() {
        with(viewModel) {

            account.observe(this@AccountActivity) {
                when (it) {
                    is State.Success -> {
                        hideLoading()
                        setUpViewUpdateAccount(it.data)
                    }
                    is State.Error -> {
                        hideLoading()
                        setUpViewCreateAccount()
                    }
                }
            }

            isFieldEmpty.observe(this@AccountActivity) { isEmpty ->
                if (isEmpty) {
                    disableButtonCreate()
                } else {
                    enableButtonCreate()
                }
            }

            action.observe(this@AccountActivity) {
                when (it) {
                    is State.Success -> {
                        hideLoading()
                        setResult(RESULT_OK)
                        finish()
                    }
                    is State.Error -> {
                        hideLoading()
                        showError()
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

    private fun setUpInputTextBalance() {
        binding.inputTextAccountBalance.apply {
            setCurrency(storage.getSymbolCurrency())
            setDecimals(false)
            setSeparator(SEPARATOR)
            addTextChangedListener {
                val balance = binding.inputTextAccountBalance.cleanDoubleValue
                viewModel.setAccountBalance(balance)
                viewModel.checkInputField()
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

    private fun setUpViewUpdateAccount(data: Account?) {
        supportActionBar?.title = getString(R.string.account_form_update_toolbar_title)
        with(binding) {
            buttonUpdate.visible()
            buttonDelete.visible()
            inputTextAccountName.setText(data?.name)
            inputTextAccountBalance.setText(data?.balance?.convertDoubleToString())
            switchPrimaryAccount.isChecked = data?.isPrimary ?: false
        }
    }

    private fun setUpViewCreateAccount() {
        supportActionBar?.title = getString(R.string.account_form_create_toolbar_title)
        with(binding) {
            buttonSave.visible()
            switchPrimaryAccount.isChecked = false
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.show()

            buttonSave.text = null
            buttonUpdate.text = null

            buttonSave.disabled()
            buttonUpdate.disabled()
            buttonDelete.disabled()
        }
    }

    private fun hideLoading() {
        with(binding) {
            progressBar.hide()

            buttonSave.text = getString(R.string.general_save)
            buttonUpdate.text = getString(R.string.general_update)

            buttonSave.enabled()
            buttonUpdate.enabled()
            buttonDelete.enabled()
        }
    }

    private fun enableButtonCreate() {
        binding.buttonSave.enabled()
    }

    private fun disableButtonCreate() {
        binding.buttonSave.disabled()
    }

    companion object {
        private const val SEPARATOR: String = "."
    }
}