package com.revolut.billing.accounts

import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.accounts.CreateAccountRequest
import org.amshove.kluent.shouldEqual
import org.junit.Test


class AccountsIT : BaseIT() {

    @Test
    fun `creates new account`() {
        val request = CreateAccountRequest(
            type = AccountType.MAIN_USER_ACCOUNT,
            subjectId = "9999",
            currency = "USD"
        )

        val response = billingAccountsClient.createAccount(request)

        val account = billingAccountsClient.getAccount(AccountType.MAIN_USER_ACCOUNT, "9999", "USD")
        account.subjectId shouldEqual "9999"
    }
}