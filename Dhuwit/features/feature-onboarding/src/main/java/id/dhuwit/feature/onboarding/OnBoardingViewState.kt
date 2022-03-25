package id.dhuwit.feature.onboarding

import id.dhuwit.state.ViewState

sealed class OnBoardingViewState : ViewState.Feature() {
    object SuccessCreateAccount : OnBoardingViewState()
    data class ValidationRequirement(val isEmpty: Boolean) : OnBoardingViewState()
}
