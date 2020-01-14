package com.revolut.billing.accounts

import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.accounts.CreateAccountRequest
import com.revolut.billing.utils.httpStatus
import com.revolut.billing.utils.shouldThrowBadRequest
import com.revolut.billing.utils.shouldThrowNotFound
import feign.FeignException
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.with
import org.junit.Test
import java.math.BigDecimal

class AccountsIT : BaseIT() {

    @Test
    fun `new account with zero balance can be created and fetched`() {
        // Arrange
        val user = generateUser()
        val request = CreateAccountRequest(AccountType.MAIN_USER_ACCOUNT, user, DEFAULT_CURRENCY)

        // Act
        accountsClient.createAccount(request)

        // Assert
        val account = fetchAccountForUser(user)
        validateAccount(account, user, AccountType.MAIN_USER_ACCOUNT, BigDecimal.ZERO)
    }

    @Test
    fun `request for nonexistent account causes http_404 (not_found)`() {
        // Act
        val action = { fetchAccountForUser("no_such_user") }

        // Assert
        action shouldThrow FeignException::class with httpStatus(404)
    }

    @Test
    fun `invalid requests cause http_4** (bad_request or not_found)`() {
        val user = generateUser()

        shouldThrowBadRequest { accountsClient.createAccount(CreateAccountRequest(AccountType.MAIN_USER_ACCOUNT, "", DEFAULT_CURRENCY)) }
        shouldThrowBadRequest { accountsClient.createAccount(CreateAccountRequest(AccountType.MAIN_USER_ACCOUNT, user, "")) }
        shouldThrowBadRequest { accountsClient.createAccount(CreateAccountRequest(AccountType.MAIN_USER_ACCOUNT, user, "USDUSD")) }
        shouldThrowBadRequest { accountsClient.getAccount(AccountType.MAIN_USER_ACCOUNT, user, "USDUSD") }
        shouldThrowNotFound { accountsClient.getAccount(AccountType.MAIN_USER_ACCOUNT, "", DEFAULT_CURRENCY) }
        shouldThrowNotFound { accountsClient.getAccount(AccountType.MAIN_USER_ACCOUNT, user, "") }
    }
}