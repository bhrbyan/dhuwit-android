package id.dhuwit.feature.category

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.category.CategoryListConstants.KEY_CATEGORY_ID
import id.dhuwit.feature.category.CategoryListConstants.KEY_CATEGORY_TYPE
import id.dhuwit.feature.category.adapter.CategoryListAdapter
import id.dhuwit.feature.category.adapter.CategoryListListener
import id.dhuwit.feature.category.databinding.CategoryListActivityBinding
import id.dhuwit.state.State
import id.dhuwit.uikit.widget.DividerMarginItemDecoration

@AndroidEntryPoint
class CategoryListActivity : BaseActivity(), CategoryListListener {

    private lateinit var binding: CategoryListActivityBinding
    private lateinit var adapterCategoryList: CategoryListAdapter
    private val viewModel: CategoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CategoryListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()

        val categoryType = intent.getStringExtra(KEY_CATEGORY_TYPE)
        viewModel.getCategories(CategoryType.getCategoryType(categoryType))

        binding.imageClose.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        observer()
    }

    override fun observer() {
        viewModel.categories.observe(this) {
            when (it) {
                is State.Loading -> showLoading()
                is State.Success -> {
                    hideLoading()
                    if (it.data.isNullOrEmpty()) {
                        showMessageEmptyCategories()
                    } else {
                        adapterCategoryList.submitList(it.data)
                        hideMessageEmptyCategories()
                    }
                }
                is State.Error -> {
                    hideLoading()
                    showError()
                }
            }
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
                DividerMarginItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL,
                    resources.getDimensionPixelSize(R.dimen.uikit_margin_padding_size_medium)
                )
            )
        }
    }

    override fun onSelectCategory(category: Category) {
        val data = Intent().apply { putExtra(KEY_CATEGORY_ID, category.id) }
        setResult(RESULT_OK, data)
        finish()
    }

    private fun showMessageEmptyCategories() {
        with(binding) {
            progressBar.hide()
            recyclerViewCategory.gone()
            textMessageEmptyCategory.visible()
        }
    }

    private fun hideMessageEmptyCategories() {
        with(binding) {
            progressBar.hide()
            recyclerViewCategory.visible()
            textMessageEmptyCategory.gone()
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.show()
            recyclerViewCategory.gone()
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