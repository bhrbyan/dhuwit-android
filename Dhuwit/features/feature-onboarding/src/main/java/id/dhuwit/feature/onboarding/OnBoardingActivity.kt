package id.dhuwit.feature.onboarding

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.enabled
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.onboarding.databinding.OnBoardingActivityBinding
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity() {

    private lateinit var binding: OnBoardingActivityBinding
    private val viewModel: OnBoardingViewModel by viewModels()

    @Inject
    lateinit var accountRouter: AccountRouter

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = OnBoardingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        viewModel.checkInputField()
    }

    override fun listener() {
        // Do nothing
    }

    override fun observer() {
        with(viewModel) {

            viewState.observe(this@OnBoardingActivity) {
                when (it) {
                    is OnBoardingViewState.SuccessCreateAccount -> {
                        viewModel.updateStatusFirstTimeUser()
                        openDashboardPage()
                    }
                    is OnBoardingViewState.ValidationRequirement -> {
                        if (it.isEmpty) {
                            disableButtonCreate()
                        } else {
                            enableButtonCreate()
                        }
                    }
                    is ViewState.Error -> {
                        showError()
                    }

                }
            }
        }
    }

    private fun setUpView() {
        with(binding) {
            inputTextAccountName.addTextChangedListener {
                viewModel.setAccountName(it.toString())
                viewModel.checkInputField()
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
            }

            buttonCreate.setOnClickListener {
                viewModel.createAccount()
            }
        }
    }

    private fun enableButtonCreate() {
        binding.buttonCreate.enabled()
    }

    private fun disableButtonCreate() {
        binding.buttonCreate.disabled()
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun openDashboardPage() {
        startActivity(accountRouter.openAccountMainPage(this))
        finish()
    }

    companion object {
        private const val SEPARATOR: String = "."
    }
}