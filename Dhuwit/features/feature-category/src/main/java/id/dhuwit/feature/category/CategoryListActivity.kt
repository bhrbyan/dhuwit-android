package id.dhuwit.feature.category

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
import id.dhuwit.feature.category.CategoryListConstants.KEY_SELECT_CATEGORY_ID
import id.dhuwit.feature.category.CategoryListConstants.KEY_SELECT_CATEGORY_TYPE
import id.dhuwit.feature.category.adapter.CategoryListAdapter
import id.dhuwit.feature.category.adapter.CategoryListListener
import id.dhuwit.feature.category.databinding.CategoryListActivityBinding
import id.dhuwit.state.ViewState

@AndroidEntryPoint
class CategoryListActivity : BaseActivity(), CategoryListListener {

    private lateinit var binding: CategoryListActivityBinding
    private lateinit var adapterCategoryList: CategoryListAdapter
    private val viewModel: CategoryListViewModel by viewModels()

    override fun init() {
        binding = CategoryListActivityBinding.inflate(layoutInflater)
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

            layoutAddCategory.setOnClickListener {
                val categoryName: String = inputTextSearch.text
                    .toString()
                    .replaceFirstChar { char -> char.uppercase() }

                viewModel.addCategory(categoryName)
            }
        }
    }

    override fun observer() {
        with(viewModel) {

            viewState.observe(this@CategoryListActivity) {
                when (it) {
                    is CategoryListViewState.GetCategories -> {
                        hideLoading()
                        if (it.categories?.isNullOrEmpty() == true) {
                            showMessageEmptyCategories(getString(R.string.category_list_message_empty))
                        } else {
                            adapterCategoryList.updateList(it.categories)
                            hideMessageEmptyCategories()
                        }
                    }
                    is CategoryListViewState.AddCategory -> {
                        onSelectCategory(it.category)
                    }
                    is CategoryListViewState.SearchCategory -> {
                        if (it.searchCategory.categories.isNullOrEmpty()) {
                            showMessageEmptyCategories(getString(R.string.category_list_message_not_found))
                            showMessageAddCategory(it.searchCategory.keywords)
                        } else {
                            adapterCategoryList.updateList(it.searchCategory.categories)
                            hideMessageEmptyCategories()
                            hideMessageAddCategory()
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
            listener = this@CategoryListActivity
        }
        binding.recyclerViewCategory.apply {
            adapter = adapterCategoryList
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    this@CategoryListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onSelectCategory(category: Category?) {
        val data = Intent().apply {
            putExtra(KEY_SELECT_CATEGORY_ID, category?.id)
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

    private fun showMessageAddCategory(keyword: String) {
        with(binding) {
            layoutAddCategory?.visible()
            textMessageAddCategory?.text =
                getString(R.string.category_list_message_add_category, keyword)
        }
    }

    private fun hideMessageAddCategory() {
        with(binding) {
            layoutAddCategory?.gone()
            textMessageAddCategory?.text = null
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.show()
            recyclerViewCategory.gone()
            textMessageEmptyCategory.gone()
            layoutAddCategory?.gone()
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