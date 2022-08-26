package id.dhuwit.core.setting.user

import android.content.Context
import id.dhuwit.core.setting.user.SettingUserConst.DEFAULT_CURRENCY
import id.dhuwit.core.setting.user.SettingUserConst.KEY_FIRST_TIME_USER
import id.dhuwit.core.setting.user.SettingUserConst.KEY_SYMBOL_CURRENCY
import id.dhuwit.core.setting.user.SettingUserConst.USER_SHARED_PREF_NAME
import javax.inject.Inject

internal class SettingUserImpl @Inject constructor(private val context: Context) : SettingUser {

    private val userSharedPref =
        context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE)

    override fun setFirstTimeUser(isFirstTimeUser: Boolean) {
        with(userSharedPref.edit()) {
            putBoolean(KEY_FIRST_TIME_USER, isFirstTimeUser)
            apply()
        }
    }

    override fun isFirstTimeUser(): Boolean {
        return userSharedPref.getBoolean(KEY_FIRST_TIME_USER, true)
    }

    override fun setSymbolCurrency(symbolCurrency: String) {
        with(userSharedPref.edit()) {
            putString(KEY_SYMBOL_CURRENCY, symbolCurrency)
            apply()
        }
    }

    override fun getSymbolCurrency(): String {
        return userSharedPref.getString(KEY_SYMBOL_CURRENCY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
    }
}