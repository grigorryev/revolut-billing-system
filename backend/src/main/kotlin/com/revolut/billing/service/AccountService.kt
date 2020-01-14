package com.revolut.billing.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.revolut.billing.DbClient
import com.revolut.billing.domain.Account
import com.revolut.billing.domain.AccountId
import com.revolut.billing.exception.NoAccountFoundException
import com.revolut.billing.repository.AccountRepository
import org.jooq.DSLContext
import org.jooq.impl.DSL

@Singleton
class AccountService @Inject constructor(
    private val accountRepository: AccountRepository,
    private val db: DbClient
) {
    fun getAccount(accountId: AccountId): Account {
        return accountRepository.findByAccountId(accountId, db.ctx())
            ?: throw NoAccountFoundException()
    }

    fun getOrCreateAccount(accountId: AccountId) = getOrCreateAccount(accountId, db.ctx())

    fun getOrCreateAccount(accountId: AccountId, ctx: DSLContext): Account {
        return ctx.transactionResult { conf ->
            accountRepository.findByAccountId(accountId, DSL.using(conf))
                ?: accountRepository.saveNewAccount(accountId, DSL.using(conf))
        }
    }

    fun updateAccount(account: Account) = accountRepository.update(account, db.ctx())

    fun updateAccount(account: Account, ctx: DSLContext) {
        accountRepository.update(account, ctx)
    }
}