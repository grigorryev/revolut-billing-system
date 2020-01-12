package com.revolut.billing.service

import com.revolut.billing.domain.Account
import com.revolut.billing.domain.AccountId

interface AccountService {
    fun getAccount(accountId: AccountId): Account
    fun getOrCreateAccount(accountId: AccountId): Account
}