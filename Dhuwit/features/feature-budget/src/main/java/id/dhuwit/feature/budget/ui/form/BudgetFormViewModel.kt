package id.dhuwit.feature.budget.ui.form

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.repository.BudgetDataSource
import javax.inject.Inject

@HiltViewModel
class BudgetFormViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource
) : ViewModel() {
}