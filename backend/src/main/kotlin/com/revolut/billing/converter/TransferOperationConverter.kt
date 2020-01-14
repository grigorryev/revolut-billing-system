package com.revolut.billing.converter

import com.revolut.billing.domain.AccountId
import com.revolut.billing.domain.AccountType
import com.revolut.billing.domain.OperationType
import com.revolut.billing.domain.Transaction
import com.revolut.billing.domain.operation.TransferOperation
import java.math.BigDecimal

object TransferOperationConverter {
    // todo: move to config
    private val TRANSFER_COMISSION_PERCENT = BigDecimal.valueOf(0.02)

    fun convert(operation: TransferOperation): List<Transaction> {
        val transferTransaction = Transaction(
            operationId = operation.operationId,
            operationType = OperationType.TRANSFER,
            accountIdFrom = operation.accountFrom,
            accountIdTo = operation.accountTo,
            amount = operation.amount
        )

        val comissionTransaction = Transaction(
            operationId = operation.operationId,
            operationType = OperationType.TRANSFER,
            accountIdFrom = operation.accountFrom,
            accountIdTo = AccountId(AccountType.TRANSFER_COMMISSION, operation.accountFrom.subjectId, operation.currency),
            amount = operation.amount * TRANSFER_COMISSION_PERCENT
        )

        return listOf(transferTransaction, comissionTransaction)
    }
}