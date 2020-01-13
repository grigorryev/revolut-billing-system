package com.revolut.billing.accounts

import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.accounts.CreateAccountRequest
import org.junit.Test
import java.math.BigDecimal

class AccountsIT : BaseIT() {

    @Test
    fun `new account with zero balance can be created and fetched`() {
        val user = generateUser()
        val request = CreateAccountRequest(AccountType.MAIN_USER_ACCOUNT, user, DEFAULT_CURRENCY)
        accountsClient.createAccount(request)

        val account = fetchAccountForUser(user)
        validateAccount(account, user, AccountType.MAIN_USER_ACCOUNT, BigDecimal.ZERO)
    }
}