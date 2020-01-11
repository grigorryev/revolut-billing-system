package com.revolut.billing.accounts

import com.revolut.billing.api.v1.CreateAccountRequest
import org.amshove.kluent.shouldEqual
import org.junit.Test


class AccountsIT : BaseIT() {

    @Test
    fun `adds account`() {
        val request = CreateAccountRequest(
            details = "9999",
            currency = "USD"
        )
        val response = billingAccountsClient.createAccount(request)
        response.accountId shouldEqual "111"
    }
}