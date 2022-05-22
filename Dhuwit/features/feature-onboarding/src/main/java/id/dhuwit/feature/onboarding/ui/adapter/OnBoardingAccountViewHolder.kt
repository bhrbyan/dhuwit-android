package id.dhuwit.feature.onboarding.ui.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.onboarding.R
import id.dhuwit.feature.onboarding.databinding.OnBoardingAccountItemBinding

class OnBoardingAccountViewHolder(private val binding: OnBoardingAccountItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val context = binding.root.context

    fun onBind(account: Account) {
        binding.textTitle.text = account.name

        when (account.name) {
            context.getString(R.string.on_boarding_default_account_checking_title) -> {
                binding.layoutContent.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.wispPink
                    )
                )

                binding.imageIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_wallet
                    )
                )
            }
            context.getString(R.string.on_boarding_default_account_credit_card_title) -> {
                binding.layoutContent.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pattensBlue
                    )
                )
                binding.imageIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_credit_card
                    )
                )
            }
            context.getString(R.string.on_boarding_default_account_cash_title) -> {
                binding.layoutContent.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.loafer
                    )
                )
                binding.imageIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_cash
                    )
                )
            }
            context.getString(R.string.on_boarding_default_account_other_title) -> {
                binding.layoutContent.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.travertine
                    )
                )
                binding.imageIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_bank
                    )
                )
            }
            else -> throw Exception("Unknown Account Name!")
        }
    }

}