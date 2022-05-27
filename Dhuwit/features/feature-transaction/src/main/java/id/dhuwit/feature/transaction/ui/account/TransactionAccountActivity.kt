package id.dhuwit.feature.transaction.ui.account

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.transaction.R
import id.dhuwit.feature.transaction.databinding.TransactionAccountActivityBinding
import id.dhuwit.feature.transaction.ui.TransactionConstants
import id.dhuwit.feature.transaction.ui.account.adapter.TransactionAccountAdapter
import id.dhuwit.feature.transaction.ui.account.adapter.TransactionAccountListener
import id.dhuwit.state.ViewState
import id.dhuwit.uikit.databinding.ToolbarBinding
import id.dhuwit.uikit.divider.DividerMarginItemDecoration

@AndroidEntryPoint
class TransactionAccountActivity : BaseActivity(), TransactionAccountListener {

    private lateinit var binding: TransactionAccountActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var adapterAccount: TransactionAccountAdapter

    private val viewModel: TransactionAccountViewModel by viewModels()

    override fun init() {
        binding = TransactionAccountActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        setContentView(binding.root)

        setUpToolbar()
        setUpAdapter()
    }

    override fun listener() {

    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is TransactionAccountViewState.GetAccounts -> {
                    adapterAccount.submitList(it.accounts)
                }
                is ViewState.Error -> {
                    showError()
                }
            }
        }
    }

    private fun setUpToolbar() {
        bindingToolbar.apply {
            textTitle.text = getString(R.string.transaction_account_toolbar_title)
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

    private fun setUpAdapter() {
        adapterAccount = TransactionAccountAdapter().apply {
            listener = this@TransactionAccountActivity
        }

        binding.recyclerView.apply {
            adapter = adapterAccount
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
            putExtra(TransactionConstants.KEY_ACCOUNT_ID, accountId)
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