package id.dhuwit.feature.onboarding.ui

import id.dhuwit.core.base.state.ViewState

sealed class OnBoardingViewState : ViewState.Feature() {

    object SuccessCreateAccount : OnBoardingViewState()

    data class ValidationRequirement(val isEmpty: Boolean) : OnBoardingViewState()

}
