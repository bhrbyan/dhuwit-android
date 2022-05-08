package id.dhuwit.feature.category.ui.budget

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.category.R
import id.dhuwit.feature.category.adapter.CategoryListAdapter
import id.dhuwit.feature.category.adapter.CategoryListListener
import id.dhuwit.feature.category.databinding.CategoryBudgetListActivityBinding
import id.dhuwit.feature.category.ui.budget.CategoryBudgetListConstants.KEY_SELECT_CATEGORY_ID
import id.dhuwit.feature.category.ui.budget.CategoryBudgetListConstants.KEY_SELECT_CATEGORY_NAME
import id.dhuwit.feature.category.ui.budget.CategoryBudgetListConstants.KEY_SELECT_CATEGORY_TYPE
import id.dhuwit.state.ViewState

@AndroidEntryPoint
class CategoryBudgetListActivity : BaseActivity(), CategoryListListener {

    private lateinit var binding: CategoryBudgetListActivityBinding
    private lateinit var adapterCategoryList: CategoryListAdapter
    private val viewModel: CategoryBudgetListViewModel by viewModels()

    override fun init() {
        binding = CategoryBudgetListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        initAdapter()
    }

    override fun listener() {
        with(binding) {
            inputTextSearch.addTextChangedListener {
                val keywords = it?.toString()?.lowercase() ?: ""
                viewModel.searchCategories(keywords)
            }
        }
    }

    override fun observer() {
        with(viewModel) {
            viewState.observe(this@CategoryBudgetListActivity) {
                when (it) {
                    is CategoryBudgetListViewState.GetCategories -> {
                        hideLoading()
                        if (it.categories?.isNullOrEmpty() == true) {
                            showMessageEmptyCategories(getString(R.string.category_list_message_empty))
                        } else {
                            adapterCategoryList.updateList(it.categories)
                            hideMessageEmptyCategories()
                        }
                    }
                    is CategoryBudgetListViewState.SearchCategory -> {
                        if (it.searchCategory.categories.isNullOrEmpty()) {
                            showMessageEmptyCategories(getString(R.string.category_list_message_not_found))
                        } else {
                            adapterCategoryList.updateList(it.searchCategory.categories)
                            hideMessageEmptyCategories()
                        }
                    }
                    is ViewState.Error -> {
                        showError()
                    }
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.category_list_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun initAdapter() {
        adapterCategoryList = CategoryListAdapter().apply {
            listener = this@CategoryBudgetListActivity
        }
        binding.recyclerViewCategory.apply {
            adapter = adapterCategoryList
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    this@CategoryBudgetListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onSelectCategory(category: Category?) {
        val data = Intent().apply {
            putExtra(KEY_SELECT_CATEGORY_ID, category?.id)
            putExtra(KEY_SELECT_CATEGORY_NAME, category?.name)
            putExtra(KEY_SELECT_CATEGORY_TYPE, category?.type.toString())
        }

        setResult(RESULT_OK, data)
        finish()
    }

    private fun showMessageEmptyCategories(message: String) {
        with(binding) {
            progressBar.hide()
            recyclerViewCategory.gone()
            textMessageEmptyCategory.apply {
                text = message
                visible()
            }
        }
    }

    private fun hideMessageEmptyCategories() {
        with(binding) {
            progressBar.hide()
            recyclerViewCategory.visible()
            textMessageEmptyCategory.apply {
                text = null
                gone()
            }
        }
    }

    private fun hideLoading() {
        with(binding) {
            progressBar.hide()
            recyclerViewCategory.visible()
        }
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.general_error_message, Snackbar.LENGTH_SHORT).show()
    }

}