package id.dhuwit.feature.main

import id.dhuwit.state.ViewState

sealed class MainViewState : ViewState.Feature() {
    object OpenOverviewPage : MainViewState()
    object OpenAccountPage : MainViewState()
    object OpenBudgetPage : MainViewState()
}