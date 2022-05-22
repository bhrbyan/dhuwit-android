package id.dhuwit.feature.onboarding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.onboarding.databinding.OnBoardingAccountItemBinding

class OnBoardingAccountAdapter : ListAdapter<Account, OnBoardingAccountViewHolder>(
    object : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
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
                    listener?.onSelectAccount(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OnBoardingAccountViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}