package id.dhuwit.feature.account.ui.main.adapter.main

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.base.extension.gone
import id.dhuwit.core.base.extension.visible
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainItemBinding

class AccountMainViewHolder(private val binding: AccountMainItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(account: Account, settingUser: SettingUser) {
        binding.textAccountName.text =
            binding.root.context.getString(R.string.account_main_account_name, account.name)
        binding.textAccountBalance.text =
            account.balance.convertPriceWithCurrencyFormat(settingUser.getSymbolCurrency())

        if (account.isPrimary) {
            binding.imagePrimaryAccount.visible()
        } else {
            binding.imagePrimaryAccount.gone()
        }
    }

}