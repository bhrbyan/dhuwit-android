package id.dhuwit.feature.main.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.main.MainActivity

object MainRouterImpl : MainRouter {
    override fun openMainPage(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }
}