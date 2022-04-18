package id.dhuwit.feature.account.ui.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountListFragmentBinding
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class AccountListFragment : BaseFragment(), AccountListListener {

    private lateinit var adapterAccount: AccountListAdapter

    private var binding: AccountListFragmentBinding? = null
    private val viewModelAccountList: AccountListViewModel by viewModels()

    @Inject
    lateinit var accountRouter: AccountRouter

    @Inject
    lateinit var storage: Storage

    private val accountResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            viewModelAccountList.getAccounts()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AccountListFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun init() {
        setUpAdapterAccount()
    }

    override fun listener() {

    }

    override fun observer() {
        viewModelAccountList.viewState.observe(this) {
            when (it) {
                is AccountListViewState.GetAccounts -> {
                    val sortedAccount =
                        it.accounts?.sortedByDescending { account -> account.isPrimary }
                    setUpDataAccount(sortedAccount)
                }
                is ViewState.Error -> {
                    showError(
                        getString(R.string.general_error_message)
                    )
                }
            }
        }
    }

    private fun setUpAdapterAccount() {
        adapterAccount = AccountListAdapter(storage).apply {
            listener = this@AccountListFragment
        }

        val orientation: Int = resources.configuration.orientation
        val layoutManagerOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager.VERTICAL
        } else {
            LinearLayoutManager.VERTICAL
        }

        val snapHelper: SnapHelper = LinearSnapHelper()
        binding?.recyclerViewAccount?.apply {
            adapter = adapterAccount
            layoutManager = LinearLayoutManager(context, layoutManagerOrientation, false)
            snapHelper.attachToRecyclerView(this)
        }
    }

    override fun onClickAccount(accountId: Long?) {
        openAccountPage(accountId)
    }

    override fun onClickAddAccount() {
        openAccountPage(null)
    }

    private fun setUpDataAccount(accounts: List<Account>?) {
        accounts?.let {
            adapterAccount.updateAccounts(accounts)
        }
    }

    private fun openAccountPage(accountId: Long?) {
        accountResult.launch(accountRouter.openAccountPage(requireContext(), accountId))
    }

    private fun showError(message: String) {
        binding?.root?.let { view ->
            Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    fun updateDataAccount() {
        viewModelAccountList.getAccounts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}