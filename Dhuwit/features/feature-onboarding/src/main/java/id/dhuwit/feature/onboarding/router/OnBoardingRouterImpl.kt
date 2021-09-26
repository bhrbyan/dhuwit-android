package id.dhuwit.feature.onboarding.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.onboarding.OnBoardingActivity

internal object OnBoardingRouterImpl : OnBoardingRouter {
    override fun openOnBoardingPage(context: Context): Intent {
        return Intent(context, OnBoardingActivity::class.java)
    }
}