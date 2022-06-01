package id.dhuwit.core.transaction.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transaction_table")
    suspend fun getTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transaction_table WHERE account_id = :accountId")
    suspend fun getTransactionsByAccountId(accountId: Long?): List<TransactionEntity>

    @Query("SELECT * FROM transaction_table WHERE id = :id")
    suspend fun getTransaction(id: Long): TransactionEntity

    @Insert
    suspend fun saveTransaction(transactionEntity: TransactionEntity)

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Query("DELETE FROM transaction_table WHERE id = :id")
    suspend fun deleteTransaction(id: Long)

    @Query("DELETE FROM transaction_table WHERE account_id = :accountId")
    suspend fun deleteTransactionByAccountId(accountId: Long?)
}