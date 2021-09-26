package id.dhuwit.feature.dashboard.router

import android.content.Context
import android.content.Intent

interface DashboardRouter {
    fun openDashboardPage(context: Context): Intent
}