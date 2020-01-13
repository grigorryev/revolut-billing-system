package com.revolut.billing.accounts

import com.revolut.billing.api.v1.dto.accounts.Account
import com.revolut.billing.api.v1.dto.accounts.AccountId
import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.transaction.OperationType
import com.revolut.billing.api.v1.dto.transaction.Transaction
import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.utils.httpStatus
import feign.FeignException
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.with
import org.junit.Test
import java.math.BigDecimal
import java.util.UUID

class DepositIT : BaseIT() {
    @Test
    fun `first deposit creates account for user`() {
        // Arrange
        val user = generateUser()

        // Act
        val operationId = depositForUser(user)

        // Assert (account created & balance increased)
        val account = fetchAccountForUser(user)
        validateAccount(account, user, AccountType.MAIN_USER_ACCOUNT, DEFAULT_DEPOSIT_AMOUNT)

        // Assert (transaction processed)
        val transactions = transactionsClient.getTransactionsByOperationId(operationId)
        transactions.size shouldEqual 1
        validateDepositTransaction(transactions[0], user, operationId)
    }

    @Test
    fun `user can deposit twice for the same account`() {
        // Arrange
        val user = generateUser()

        // Act
        val operationId1 = depositForUser(user)
        val operationId2 = depositForUser(user)

        // Assert (amount x2 deposited)
        val account = fetchAccountForUser(user)
        validateAccount(account, user, AccountType.MAIN_USER_ACCOUNT, DEFAULT_DEPOSIT_AMOUNT * BigDecimal.valueOf(2))
    }

    @Test
    fun `second deposit with the same operationId should be rejected`() {
        // Arrange (deposit 1st time)
        val user = generateUser()
        val operationId = depositForUser(user)

        // Act (deposit 2nd time)
        val action = { depositForUser(user, operationId) }

        // Assert (2nd deposit is rejected)
        action shouldThrow FeignException::class with httpStatus(409)

        // Assert (only one deposit was processed)
        val account = fetchAccountForUser(user)
        validateAccount(account, user, AccountType.MAIN_USER_ACCOUNT, DEFAULT_DEPOSIT_AMOUNT)
    }

    @Test
    fun `empty userId causes http 404`() {
        // Arrange
        val request = DepositRequest(UUID.randomUUID(), "", DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, DEFAULT_CURRENCY)

        // Act
        val action = { operationsClient.deposit(request) }

        // Assert
        action shouldThrow FeignException::class with httpStatus(404)
    }

    @Test
    fun `empty paymentSystem causes http 404`() {
        // Arrange
        val user = generateUser()
        val request = DepositRequest(UUID.randomUUID(), user, "", DEFAULT_DEPOSIT_AMOUNT, DEFAULT_CURRENCY)

        // Act
        val action = { operationsClient.deposit(request) }

        // Assert
        action shouldThrow FeignException::class with httpStatus(404)
    }

    @Test
    fun `empty currency causes http 404`() {
        // Arrange
        val user = generateUser()
        val request = DepositRequest(UUID.randomUUID(), user, DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, "")

        // Act
        val action = { operationsClient.deposit(request) }

        // Assert
        action shouldThrow FeignException::class with httpStatus(404)
    }

    @Test
    fun `invalid currency causes http 404`() {
        // Arrange
        val user = generateUser()
        val request = DepositRequest(UUID.randomUUID(), user, DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, "USDUSD")

        // Act
        val action = { operationsClient.deposit(request) }

        // Assert
        action shouldThrow FeignException::class with httpStatus(404)
    }

    @Test
    fun `negative amount causes http 404`() {
        // Arrange
        val user = generateUser()
        val request = DepositRequest(UUID.randomUUID(), user, DEFAULT_PAYMENT_SYSTEM, BigDecimal.valueOf(-1), DEFAULT_CURRENCY)

        // Act
        val action = { operationsClient.deposit(request) }

        // Assert
        action shouldThrow FeignException::class with httpStatus(404)
    }

    private fun depositForUser(userId: String, operationId: UUID = UUID.randomUUID()): UUID {
        val request = DepositRequest(operationId, userId, DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, DEFAULT_CURRENCY)
        return operationsClient.deposit(request).operationId
    }

    private fun validateDepositTransaction(tx: Transaction, userId: String, operationId: UUID) {
        with(tx) {
            operationId shouldEqual operationId
            operationType shouldEqual OperationType.DEPOSIT
            accountIdFrom shouldEqual AccountId(AccountType.PAYMENT_SYSTEM, DEFAULT_PAYMENT_SYSTEM, DEFAULT_CURRENCY)
            accountIdTo shouldEqual AccountId(AccountType.MAIN_USER_ACCOUNT, userId, DEFAULT_CURRENCY)
            amount.stripTrailingZeros() shouldEqual DEFAULT_DEPOSIT_AMOUNT.stripTrailingZeros()
        }
    }
}