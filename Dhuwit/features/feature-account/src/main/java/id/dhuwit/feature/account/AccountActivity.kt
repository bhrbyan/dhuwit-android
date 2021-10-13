package id.dhuwit.feature.account

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertDoubleToString
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.enabled
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

    }

    override fun observer() {
        with(viewModel) {
            account.observe(this@AccountActivity) {
                when (it) {
                    is State.Loading -> {
                        showLoading()
                    }
                    is State.Success -> {
                        setUpView(it.data)
                        hideLoading()
                    }
                    is State.Error -> {
                        hideLoading()
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

            updateAccount.observe(this@AccountActivity) {
                when (it) {
                    is State.Loading -> {
                        showLoading()
                    }
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
        supportActionBar?.title = getString(R.string.account_hint_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setUpView(data: Account?) {
        with(binding) {
            inputTextAccountName.apply {
                addTextChangedListener {
                    viewModel.setAccountName(it.toString())
                    viewModel.checkInputField()
                }
                setText(data?.name)
            }

            inputTextAccountBalance.apply {
                setCurrency(storage.getSymbolCurrency())
                setDecimals(false)
                setSeparator(SEPARATOR)
                addTextChangedListener {
                    val balance = binding.inputTextAccountBalance.cleanDoubleValue
                    viewModel.setAccountBalance(balance)
                    viewModel.checkInputField()
                }

                setText(data?.balance?.convertDoubleToString())
            }

            buttonSave.setOnClickListener {
                viewModel.saveAccount()
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.show()
            buttonSave.text = null
            buttonSave.disabled()
        }
    }

    private fun hideLoading() {
        with(binding) {
            progressBar.hide()
            buttonSave.text = getString(R.string.general_save)
            buttonSave.enabled()
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