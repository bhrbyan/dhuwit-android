package id.dhuwit.storage

interface Storage {
    fun setFirstTimeUser(isFirstTimeUser: Boolean)
    fun isFirstTimeUser(): Boolean
    fun setSymbolCurrency(symbolCurrency: String)
    fun getSymbolCurrency(): String
}