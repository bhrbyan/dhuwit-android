package id.dhuwit.feature.onboarding.router

import android.content.Context
import android.content.Intent

interface OnBoardingRouter {
    fun openOnBoardingPage(context: Context): Intent
}