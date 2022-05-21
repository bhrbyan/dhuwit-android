package id.dhuwit.core.transaction.repository

import id.dhuwit.core.transaction.model.GetTransactionType
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.state.State

interface TransactionDataSource {
    suspend fun getTransactions(getTransactionType: GetTransactionType): State<List<Transaction>>
    suspend fun getTransaction(id: Long): State<Transaction>
    suspend fun saveTransaction(transaction: Transaction): State<Boolean>
    suspend fun updateTransaction(
        transaction: Transaction,
        existingTransaction: Transaction?
    ): State<Boolean>

    suspend fun deleteTransaction(transactionId: Long): State<Boolean>
}