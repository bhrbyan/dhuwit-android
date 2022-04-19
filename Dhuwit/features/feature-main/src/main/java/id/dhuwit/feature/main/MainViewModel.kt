package id.dhuwit.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.dhuwit.state.ViewState

class MainViewModel : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        openOverviewPage()
    }

    fun openOverviewPage() {
        updateViewState(MainViewState.OpenOverviewPage)
    }

    fun openAccountPage() {
        updateViewState(MainViewState.OpenAccountPage)
    }

    fun openBudgetPage() {
        updateViewState(MainViewState.OpenBudgetPage)
    }
}