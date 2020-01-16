package com.revolut.billing.repository

import com.google.inject.Singleton
import com.revolut.billing.domain.Account
import com.revolut.billing.domain.AccountId
import db.tables.Account.ACCOUNT
import org.jooq.DSLContext
import java.math.BigDecimal

@Singleton
class AccountRepository {
    fun findByAccountId(accountId: AccountId, ctx: DSLContext): Account? {
        val record = ctx.select().from(ACCOUNT)
            .where(ACCOUNT.ACCOUNT_TYPE.eq(accountId.type.toString()))
            .and(ACCOUNT.SUBJECT_ID.eq(accountId.subjectId))
            .and(ACCOUNT.CURRENCY.eq(accountId.currency))
            .fetchOne()

        return if (record == null) null else Account.fromDbRecord(record)
    }

    fun saveNewAccount(accountId: AccountId, ctx: DSLContext): Account {
        val id = ctx.insertInto(ACCOUNT, ACCOUNT.ACCOUNT_TYPE, ACCOUNT.SUBJECT_ID, ACCOUNT.CURRENCY, ACCOUNT.AMOUNT)
            .values(accountId.type.toString(), accountId.subjectId, accountId.currency, BigDecimal.ZERO)
            .returning(ACCOUNT.ID)
            .fetchOne()
            .id

        return Account(id, accountId, BigDecimal.ZERO)
    }

    fun update(account: Account, ctx: DSLContext) {
        ctx.update(ACCOUNT)
            .set(ACCOUNT.AMOUNT, account.amount)
            .where(ACCOUNT.ID.eq(account.id))
            .execute()
    }
}