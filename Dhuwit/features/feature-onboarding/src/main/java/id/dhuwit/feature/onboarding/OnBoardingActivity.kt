package id.dhuwit.feature.onboarding

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.disabled
import id.dhuwit.core.extension.enabled
import id.dhuwit.feature.dashboard.router.DashboardRouter
import id.dhuwit.feature.onboarding.databinding.OnBoardingActivityBinding
import id.dhuwit.state.State
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity() {

    private lateinit var binding: OnBoardingActivityBinding
    private val viewModel: OnBoardingViewModel by viewModels()

    @Inject
    lateinit var dashboardRouter: DashboardRouter

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
            isFieldEmpty.observe(this@OnBoardingActivity) { isEmpty ->
                if (isEmpty) {
                    disableButtonCreate()
                } else {
                    enableButtonCreate()
                }
            }

            createAccount.observe(this@OnBoardingActivity) {
                when (it) {
                    is State.Success -> {
                        hideLoading()

                        viewModel.updateStatusFirstTimeUser()
                        openDashboardPage()
                    }
                    is State.Error -> {
                        hideLoading()
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

    private fun showLoading() {
        disableButtonCreate()

        binding.progressBar.show()
        binding.buttonCreate.text = null
    }

    private fun hideLoading() {
        enableButtonCreate()

        binding.progressBar.hide()
        binding.buttonCreate.text = getString(R.string.general_create)
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun openDashboardPage() {
        startActivity(dashboardRouter.openDashboardPage(this))
        finish()
    }

    companion object {
        private const val SEPARATOR: String = "."
    }
}