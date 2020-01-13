package com.revolut.billing.domain

import db.tables.Transaction.TRANSACTION
import org.jooq.Record
import java.math.BigDecimal
import java.util.UUID
import com.revolut.billing.api.v1.dto.transaction.Transaction as TransactionDto

class Transaction(
    val id: Long = 0,
    val operationId: UUID,
    val operationType: OperationType,
    val accountIdFrom: AccountId,
    val accountIdTo: AccountId,
    val amount: BigDecimal
) {
    fun toDto() = TransactionDto(
        id = this.id,
        operationId = this.operationId,
        operationType = this.operationType.toDto(),
        accountIdFrom = this.accountIdFrom.toDto(),
        accountIdTo = this.accountIdTo.toDto(),
        amount = this.amount
    )

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