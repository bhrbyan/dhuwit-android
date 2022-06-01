package id.dhuwit.feature.onboarding.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.AccountOnBoarding
import id.dhuwit.feature.onboarding.databinding.OnBoardingAccountItemBinding

class OnBoardingAccountViewHolder(private val binding: OnBoardingAccountItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(account: AccountOnBoarding) {
        binding.textTitle.text = account.name
        binding.textDescription.text = account.description
        binding.layoutContent.setBackgroundColor(account.backgroundColor)
        binding.imageIcon.setImageDrawable(account.icon)
    }

}