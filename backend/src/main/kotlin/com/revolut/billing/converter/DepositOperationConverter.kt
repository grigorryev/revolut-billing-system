package com.revolut.billing.converter

import com.revolut.billing.domain.OperationType
import com.revolut.billing.domain.Transaction
import com.revolut.billing.domain.operation.DepositOperation

/**
 * Converts DepositOperation into the list of transactions.
 */
object DepositOperationConverter {
    fun convert(operation: DepositOperation): List<Transaction> {
        val depositTransaction = Transaction(
            operationId = operation.operationId,
            operationType = OperationType.DEPOSIT,
            accountIdFrom = operation.paymentSystemAccountId,
            accountIdTo = operation.userAccountId,
            amount = operation.amount
        )

        return listOf(depositTransaction)
    }
}