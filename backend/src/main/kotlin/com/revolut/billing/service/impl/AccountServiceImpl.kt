package com.revolut.billing.service.impl

import com.google.inject.Singleton
import com.revolut.billing.MemoryDatabase
import com.revolut.billing.domain.Account
import com.revolut.billing.domain.AccountId
import com.revolut.billing.service.AccountService
import db.tables.Account.ACCOUNT
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import java.math.BigDecimal

@Singleton
class AccountServiceImpl : AccountService {

    val db = MemoryDatabase()

    override fun getAccount(accountId: AccountId): Account {
        val record = fetchByAccountId(accountId, db.ctx())

        return if (record != null)
            Account.fromDbRecord(record)
        else
            throw IllegalStateException("No account found")
    }

    override fun getOrCreateAccount(accountId: AccountId): Account {
        return db.ctx().transactionResult { configuration ->

            val record = fetchByAccountId(accountId, DSL.using(configuration))

            if (record != null) {
                Account.fromDbRecord(record)
            }

            saveNewAccount(accountId, DSL.using(configuration))
        }
    }

    private fun fetchByAccountId(accountId: AccountId, ctx: DSLContext): Record? {
        return ctx.select().from(ACCOUNT)
            .where(ACCOUNT.ACCOUNT_TYPE.eq(accountId.type.toString()))
            .and(ACCOUNT.SUBJECT_ID.eq(accountId.subjectId))
            .and(ACCOUNT.CURRENCY.eq(accountId.currency))
            .fetchOne()
    }

    private fun saveNewAccount(accountId: AccountId, ctx: DSLContext): Account {
        val id = ctx.insertInto(ACCOUNT, ACCOUNT.ACCOUNT_TYPE, ACCOUNT.SUBJECT_ID, ACCOUNT.CURRENCY, ACCOUNT.AMOUNT)
            .values(accountId.type.toString(), accountId.subjectId, accountId.currency, BigDecimal.ZERO)
            .returning(ACCOUNT.ID)
            .fetchOne()
            .id

        return Account(id, accountId, BigDecimal.ZERO)
    }
}