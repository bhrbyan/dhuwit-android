package id.dhuwit.feature.calculator.router

import CalculatorFragment
import androidx.fragment.app.Fragment

internal class CalculatorRouterImpl : CalculatorRouter {

    override fun getCalculatorFragment(): Fragment {
        return CalculatorFragment()
    }

}