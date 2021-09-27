package id.dhuwit.core.transaction.repository

import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.state.State

interface TransactionDataSource {
    suspend fun getTransactions(): State<List<Transaction>>
    suspend fun getTransaction(id: Long): State<Transaction>
    suspend fun saveTransaction(transaction: Transaction): State<Boolean?>
    suspend fun updateTransaction(transaction: Transaction): State<Boolean?>
    suspend fun deleteTransaction(id: Long): State<Boolean?>
}