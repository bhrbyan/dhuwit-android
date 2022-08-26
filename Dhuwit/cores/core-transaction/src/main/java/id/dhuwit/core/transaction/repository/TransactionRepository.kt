package id.dhuwit.core.transaction.repository

import id.dhuwit.core.base.state.State
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionDeleteBy
import id.dhuwit.core.transaction.model.TransactionGetBy
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val local: TransactionDataSource
) : TransactionDataSource {

    override suspend fun getTransactions(transactionGetBy: TransactionGetBy): State<List<Transaction>> {
        return local.getTransactions(transactionGetBy)
    }

    override suspend fun getTransaction(id: Long): State<Transaction> {
        return local.getTransaction(id)
    }

    override suspend fun saveTransaction(transaction: Transaction): State<Boolean> {
        return local.saveTransaction(transaction)
    }

    override suspend fun updateTransaction(
        transaction: Transaction,
        existingTransaction: Transaction?
    ): State<Boolean> {
        return local.updateTransaction(transaction, existingTransaction)
    }

    override suspend fun deleteTransaction(transactionDeleteBy: TransactionDeleteBy): State<Boolean> {
        return local.deleteTransaction(transactionDeleteBy)
    }
}