package id.dhuwit.feature.account.ui.selection

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.account.AccountConstants
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountSelectionActivityBinding
import id.dhuwit.feature.account.ui.selection.adapter.AccountSelectionAdapter
import id.dhuwit.feature.account.ui.selection.adapter.AccountSelectionListener
import id.dhuwit.state.ViewState
import id.dhuwit.uikit.divider.DividerMarginItemDecoration

@AndroidEntryPoint
class AccountSelectionActivity : BaseActivity(), AccountSelectionListener {

    private lateinit var binding: AccountSelectionActivityBinding
    private lateinit var adapterAccountSelection: AccountSelectionAdapter

    private val viewModel: AccountSelectionViewModel by viewModels()

    override fun init() {
        binding = AccountSelectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        setUpAdapter()
    }

    override fun listener() {

    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is AccountSelectionViewState.GetAccounts -> {
                    adapterAccountSelection.submitList(it.accounts)
                }
                is ViewState.Error -> {
                    showError()
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.account_selection_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun setUpAdapter() {
        adapterAccountSelection = AccountSelectionAdapter().apply {
            listener = this@AccountSelectionActivity
        }

        binding.recyclerView.apply {
            adapter = adapterAccountSelection
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerMarginItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL,
                    resources.getDimensionPixelSize(R.dimen.uikit_margin_padding_size_medium)
                )
            )
        }
    }

    override fun onClickAccount(accountId: Long?) {
        setResult(RESULT_OK, Intent().apply {
            putExtra(AccountConstants.KEY_ACCOUNT_ID, accountId)
        })
        finish()
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}