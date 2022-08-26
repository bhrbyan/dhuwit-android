package id.dhuwit.core.transaction.repository

import id.dhuwit.core.base.state.State
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionDeleteBy
import id.dhuwit.core.transaction.model.TransactionGetBy

interface TransactionDataSource {
    suspend fun getTransactions(transactionGetBy: TransactionGetBy): State<List<Transaction>>
    suspend fun getTransaction(id: Long): State<Transaction>
    suspend fun saveTransaction(transaction: Transaction): State<Boolean>
    suspend fun updateTransaction(
        transaction: Transaction,
        existingTransaction: Transaction?
    ): State<Boolean>

    suspend fun deleteTransaction(transactionDeleteBy: TransactionDeleteBy): State<Boolean>
}