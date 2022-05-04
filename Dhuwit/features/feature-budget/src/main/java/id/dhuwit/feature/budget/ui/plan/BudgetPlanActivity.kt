package id.dhuwit.feature.budget.ui.plan

import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.budget.R
import id.dhuwit.feature.budget.databinding.BudgetPlanActivityBinding
import id.dhuwit.feature.budget.ui.plan.adapter.BudgetPlanAdapter
import id.dhuwit.feature.budget.ui.plan.adapter.BudgetPlanListener
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class BudgetPlanActivity : BaseActivity(), BudgetPlanListener {

    private lateinit var binding: BudgetPlanActivityBinding
    private lateinit var adapterPlan: BudgetPlanAdapter
    private val viewModel: BudgetPlanViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = BudgetPlanActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
    }

    override fun listener() {
        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is BudgetPlanViewState.GetCategories -> {
                    if (it.categories.isNullOrEmpty()) {
                        // show empty state
                    } else {
                        adapterPlan.updateList(it.categories)
                    }
                }
                is ViewState.Error -> {
                    showError()
                }
            }
        }
    }

    private fun setUpAdapter() {
        adapterPlan = BudgetPlanAdapter(storage).apply {
            listener = this@BudgetPlanActivity
        }

        binding.recyclerView.apply {
            adapter = adapterPlan
            layoutManager = LinearLayoutManager(this@BudgetPlanActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@BudgetPlanActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onClickItem() {
        // Do Something
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}