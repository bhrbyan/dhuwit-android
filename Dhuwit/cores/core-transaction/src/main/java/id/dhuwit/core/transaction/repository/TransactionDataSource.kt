package id.dhuwit.core.transaction.repository

import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionAccount
import id.dhuwit.core.transaction.model.TransactionCategory
import id.dhuwit.core.transaction.model.TransactionGetType
import id.dhuwit.state.State

interface TransactionDataSource {
    suspend fun getTransactions(
        transactionGetType: TransactionGetType,
        periodDate: String? = null
    ): State<List<Transaction>>

    suspend fun getTransaction(transactionId: Long): State<Transaction>
    suspend fun saveTransaction(transaction: Transaction): State<Boolean>
    suspend fun updateTransaction(
        transaction: Transaction,
        existingTransaction: Transaction?
    ): State<Boolean>

    suspend fun deleteTransaction(transactionId: Long): State<Boolean>
    suspend fun getCategoryTransaction(
        periodDate: String?,
        categoryType: CategoryType
    ): State<List<TransactionCategory>>

    suspend fun getAccountTransaction(periodDate: String?): State<List<TransactionAccount>>
}