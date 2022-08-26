package id.dhuwit.feature.account.ui.form

import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.extension.*
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountFormActivityBinding
import id.dhuwit.feature.calculator.databinding.CalculatorBottomSheetBinding
import id.dhuwit.feature.calculator.router.CalculatorRouter
import id.dhuwit.feature.calculator.ui.CalculatorListener
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import id.dhuwit.uikit.databinding.ToolbarBinding
import javax.inject.Inject

@AndroidEntryPoint
class AccountFormActivity : id.dhuwit.core.base.base.BaseActivity(), CalculatorListener {

    private lateinit var binding: AccountFormActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var bindingCalculator: CalculatorBottomSheetBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: AccountFormViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var calculatorRouter: CalculatorRouter

    override fun init() {
        binding = AccountFormActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        bindingCalculator = binding.layoutCalculator
        setContentView(binding.root)

        setUpToolbar()
        setUpBottomSheet()
    }

    override fun listener() {
        with(binding) {
            inputTextAccountName.apply {
                addTextChangedListener {
                    validationRequirement()
                }
            }

            buttonSave.setOnClickListener {
                with(binding) {
                    viewModel.createAccount(
                        name = inputTextAccountName.text.toString(),
                        isPrimary = switchPrimaryAccount.isChecked
                    )
                }
            }

            buttonUpdate.setOnClickListener {
                viewModel.updateAccount(
                    name = inputTextAccountName.text.toString(),
                    isPrimary = switchPrimaryAccount.isChecked
                )
            }

            buttonDelete.setOnClickListener {
                viewModel.deleteAccount()
            }

            textAmount.setOnClickListener {
                showCalculator()
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

            viewModel.amount.observe(this@AccountFormActivity) {
                setTextAmount(it)
            }
        }
    }

    private fun setUpToolbar() {
        bindingToolbar.apply {
            imageActionLeft.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close))
                setOnClickListener {
                    setResult(RESULT_CANCELED)
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
            switchPrimaryAccount.isChecked = data?.isPrimary ?: false
        }
    }

    private fun validationRequirement() {
        with(binding) {
            if (inputTextAccountName.text.isNullOrEmpty()) {
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

    private fun setUpBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(bindingCalculator.layoutBottomSheetRoot).apply {
                isHideable = true
            }

        // Default State
        closeCalculator()

        supportFragmentManager.beginTransaction()
            .replace(bindingCalculator.frameLayout.id, calculatorRouter.getCalculatorFragment())
            .commit()
    }

    override fun onInputNumber(text: String) {
        viewModel.setCounter(text, true)
    }

    override fun onClear() {
        viewModel.onClearCounter()
    }

    override fun onClose() {
        closeCalculator()
    }

    private fun showCalculator() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeCalculator() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setTextAmount(amount: Double?) {
        binding.textAmount.text = amount?.convertPriceWithCurrencyFormat(
            storage.getSymbolCurrency()
        )
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            closeCalculator()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    companion object {
        private const val SEPARATOR: String = "."
    }
}