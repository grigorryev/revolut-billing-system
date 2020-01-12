package com.revolut.billing.domain

import db.tables.Transaction.TRANSACTION
import org.jooq.Record
import java.math.BigDecimal
import java.util.UUID

class Transaction(
    val id: Long = 0,
    val operationId: UUID,
    val operationType: OperationType,
    val accountIdFrom: AccountId,
    val accountIdTo: AccountId,
    val amount: BigDecimal
) {
    companion object {
        fun fromDbRecord(record: Record) = Transaction(
            id = record.getValue(TRANSACTION.ID),
            operationId = UUID.fromString(record.getValue(TRANSACTION.OPERATION_ID)),
            operationType = OperationType.valueOf(record.getValue(TRANSACTION.OPERATION_TYPE)),
            accountIdFrom = AccountId(
                type = AccountType.valueOf(record.getValue(TRANSACTION.FROM_ACCOUNT_TYPE)),
                subjectId = record.getValue(TRANSACTION.FROM_SUBJECT_ID),
                currency = record.getValue(TRANSACTION.CURRENCY)
            ),
            accountIdTo = AccountId(
                type = AccountType.valueOf(record.getValue(TRANSACTION.TO_ACCOUNT_TYPE)),
                subjectId = record.getValue(TRANSACTION.TO_SUBJECT_ID),
                currency = record.getValue(TRANSACTION.CURRENCY)
            ),
            amount = record.getValue(TRANSACTION.AMOUNT)
        )
    }
}