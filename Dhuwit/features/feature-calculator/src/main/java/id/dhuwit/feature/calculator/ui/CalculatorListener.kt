package id.dhuwit.feature.calculator.ui

interface CalculatorListener {
    fun onInputNumber(text: String)
    fun onClear()
    fun onClose()
}