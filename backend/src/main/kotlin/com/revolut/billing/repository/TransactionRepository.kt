package com.revolut.billing.repository

import com.google.inject.Singleton
import com.revolut.billing.domain.AccountId
import com.revolut.billing.domain.Transaction
import db.tables.Transaction.TRANSACTION
import org.jooq.DSLContext

@Singleton
class TransactionRepository {

    fun save(tx: Transaction, ctx: DSLContext) {
        with(TRANSACTION) {
            ctx
                .insertInto(
                    TRANSACTION,
                    OPERATION_ID,
                    OPERATION_TYPE,
                    FROM_SUBJECT_ID,
                    FROM_ACCOUNT_TYPE,
                    TO_SUBJECT_ID,
                    TO_ACCOUNT_TYPE,
                    AMOUNT,
                    CURRENCY
                )
                .values(
                    tx.operationType.toString(),
                    tx.operationId.toString(),
                    tx.accountIdFrom.subjectId,
                    tx.accountIdFrom.type.toString(),
                    tx.accountIdTo.subjectId,
                    tx.accountIdTo.type.toString(),
                    tx.amount,
                    tx.accountIdTo.currency
                )
                .returning(db.tables.Account.ACCOUNT.ID)
                .execute()
        }
    }

    fun findBySubjectId(accountId: AccountId, ctx: DSLContext): List<Transaction> {
        return ctx.selectFrom(TRANSACTION)
            .where(TRANSACTION.TO_SUBJECT_ID.eq(accountId.subjectId))
            .or(TRANSACTION.FROM_SUBJECT_ID.eq(accountId.subjectId))
            .fetch()
            .map { Transaction.fromDbRecord(it) }
    }
}