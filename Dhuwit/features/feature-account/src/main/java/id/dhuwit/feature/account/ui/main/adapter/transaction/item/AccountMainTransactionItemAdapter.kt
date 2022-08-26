package id.dhuwit.feature.account.ui.main.adapter.transaction.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.account.databinding.AccountMainTransactionItemBinding

class AccountMainTransactionItemAdapter(
    val transactions: List<Transaction>,
    private val settingUser: SettingUser,
    private val listener: AccountMainTransactionItemListener?
) : RecyclerView.Adapter<AccountMainTransactionItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountMainTransactionItemViewHolder {
        return AccountMainTransactionItemViewHolder(
            AccountMainTransactionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickTransaction(transactions[adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: AccountMainTransactionItemViewHolder, position: Int) {
        holder.onBind(transactions[position], settingUser)

    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}