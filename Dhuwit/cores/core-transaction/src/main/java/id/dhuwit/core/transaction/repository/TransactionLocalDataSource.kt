package id.dhuwit.core.transaction.repository

import id.dhuwit.core.transaction.database.TransactionDao
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionLocalDataSource @Inject constructor(
    private val dao: TransactionDao
) : TransactionDataSource {

    override suspend fun getTransactions(): State<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = dao.getTransactions().map { it.toModel() }

                State.Success(transactions)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getTransaction(id: Long): State<Transaction> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = dao.getTransaction(id).toModel()

                State.Success(transactions)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun saveTransaction(transaction: Transaction): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.saveTransaction(transaction.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.updateTransaction(transaction.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun deleteTransaction(id: Long): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.deleteTransaction(id)

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }
}