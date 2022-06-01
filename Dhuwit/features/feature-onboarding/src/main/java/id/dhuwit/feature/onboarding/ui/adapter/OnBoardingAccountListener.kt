package id.dhuwit.feature.onboarding.ui.adapter

import id.dhuwit.core.account.model.Account

interface OnBoardingAccountListener {
    fun onSelectAccount(account: Account)
}