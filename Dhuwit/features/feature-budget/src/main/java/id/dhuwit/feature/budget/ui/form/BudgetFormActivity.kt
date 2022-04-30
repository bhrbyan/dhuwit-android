package id.dhuwit.feature.budget.ui.form

import androidx.activity.viewModels
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.budget.databinding.BudgetFormActivityBinding

class BudgetFormActivity : BaseActivity() {

    private lateinit var binding: BudgetFormActivityBinding
    private val viewModel: BudgetFormViewModel by viewModels()

    override fun init() {
        binding = BudgetFormActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listener() {

    }

    override fun observer() {

    }
}