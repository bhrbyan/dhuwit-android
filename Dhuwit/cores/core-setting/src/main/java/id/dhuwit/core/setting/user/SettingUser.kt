package id.dhuwit.core.setting.user

interface SettingUser {
    fun setFirstTimeUser(isFirstTimeUser: Boolean)
    fun isFirstTimeUser(): Boolean
    fun setSymbolCurrency(symbolCurrency: String)
    fun getSymbolCurrency(): String
}