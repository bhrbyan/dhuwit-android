package id.dhuwit.feature.account

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.account.databinding.AccountActivityBinding

@AndroidEntryPoint
class AccountActivity : BaseActivity() {

    private val viewModel: AccountViewModel by viewModels()

    private lateinit var binding: AccountActivityBinding

    override fun init() {
        binding = AccountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listener() {

    }

    override fun observer() {

    }
}