package id.dhuwit.feature.onboarding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.model.AccountOnBoarding
import id.dhuwit.feature.onboarding.databinding.OnBoardingAccountItemBinding

class OnBoardingAccountAdapter : ListAdapter<AccountOnBoarding, OnBoardingAccountViewHolder>(
    object : DiffUtil.ItemCallback<AccountOnBoarding>() {
        override fun areItemsTheSame(
            oldItem: AccountOnBoarding,
            newItem: AccountOnBoarding
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: AccountOnBoarding,
            newItem: AccountOnBoarding
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    var listener: OnBoardingAccountListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingAccountViewHolder {
        return OnBoardingAccountViewHolder(
            OnBoardingAccountItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(adapterPosition)
                    listener?.onSelectAccount(
                        Account(
                            item.name,
                            DEFAULT_AMOUNT_ACCOUNT,
                            true
                        )
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OnBoardingAccountViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private const val DEFAULT_AMOUNT_ACCOUNT: Double = 0.0
    }
}