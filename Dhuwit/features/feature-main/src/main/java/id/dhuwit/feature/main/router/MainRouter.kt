package id.dhuwit.feature.main.router

import android.content.Context
import android.content.Intent

interface MainRouter {
    fun openMainPage(context: Context): Intent
}