package id.dhuwit.feature.overview.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.transaction.model.TransactionCategory
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewCategoryFragmentBinding
import id.dhuwit.feature.overview.ui.category.adapter.OverviewCategoryAdapter
import id.dhuwit.feature.overview.ui.category.adapter.OverviewCategoryListener
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class OverviewCategoryFragment : BaseFragment(), OverviewCategoryListener {

    private var binding: OverviewCategoryFragmentBinding? = null
    private val viewModel: OverviewCategoryViewModel by viewModels()

    private lateinit var adapterCategory: OverviewCategoryAdapter

    @Inject
    lateinit var storage: Storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OverviewCategoryFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun init() {
        setUpAdapter()
    }

    override fun listener() {
        binding?.let {
            it.imageNext.setOnClickListener {
                viewModel.onNextPeriodDate()
            }

            it.imagePrevious.setOnClickListener {
                viewModel.onPreviousPeriodDate()
            }
            it.imageDropDown.setOnClickListener { view ->
                showMenuCategoryType(view, R.menu.overview_menu_category_type)
            }
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is OverviewCategoryViewState.GetDetails -> {
                    setUpPeriodDate(it.periodDate)
                    setUpCategoryType(it.categoryType)

                    if (it.categoryTransaction.isNullOrEmpty()) {
                        showMessageEmptyCategory()
                    } else {
                        showList(it.categoryTransaction)
                    }
                }
                is OverviewCategoryViewState.CategoryNotFound -> {
                    showError(
                        getString(R.string.overview_categories_not_found)
                    )
                }
                is OverviewCategoryViewState.SetPeriodDate -> {
                    binding?.textMonth?.text = it.periodDate
                }
                is OverviewCategoryViewState.SetCategoryType -> {
                    setUpCategoryType(it.categoryType)
                }
                is ViewState.Error -> showError(
                    getString(R.string.general_error_message)
                )
            }
        }
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

    private fun setUpAdapter() {
        adapterCategory = OverviewCategoryAdapter(storage.getSymbolCurrency())
        binding?.recyclerView?.apply {
            adapter = adapterCategory
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onClickCategory(item: TransactionCategory) {
        Toast.makeText(requireContext(), "onClickCategory", Toast.LENGTH_SHORT).show()
    }

    private fun showMessageEmptyCategory() {
        binding?.let {
            it.recyclerView.gone()
            it.textEmptyTransaction.visible()
        }
    }

    private fun showList(categoryTransaction: List<TransactionCategory>) {
        binding?.let {
            it.recyclerView.visible()
            it.textEmptyTransaction.gone()

            adapterCategory.updateList(categoryTransaction)
        }
    }

    private fun setUpPeriodDate(periodDate: String?) {
        binding?.textMonth?.text = periodDate
    }

    private fun setUpCategoryType(categoryType: CategoryType) {
        binding?.textCategoryType?.text = when (categoryType) {
            is CategoryType.Expense -> getString(R.string.overview_category_expense_type)
            is CategoryType.Income -> getString(R.string.overview_category_income_type)
        }
    }

    private fun showMenuCategoryType(v: View, @MenuRes menuRes: Int) {
        PopupMenu(context, v).apply {
            menuInflater.inflate(menuRes, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_expense -> viewModel.setCategoryType(CategoryType.Expense)
                    R.id.menu_income -> viewModel.setCategoryType(CategoryType.Income)
                }
                true
            }
            setOnDismissListener {
                // Respond to popup being dismissed.
            }
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}