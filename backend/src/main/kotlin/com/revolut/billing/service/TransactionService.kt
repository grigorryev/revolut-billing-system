package com.revolut.billing.service

import com.google.inject.Inject
import com.revolut.billing.DbClient
import com.revolut.billing.domain.Transaction
import com.revolut.billing.repository.TransactionRepository
import java.util.UUID

class TransactionService @Inject constructor(
    private val db: DbClient,
    private val transactionRepository: TransactionRepository
) {
    fun getByOperationId(operationId: UUID): List<Transaction> {
        return transactionRepository.findByOperationId(operationId, db.ctx())
    }
}