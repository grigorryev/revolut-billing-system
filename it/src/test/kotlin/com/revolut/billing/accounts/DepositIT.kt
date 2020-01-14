package com.revolut.billing.accounts

import com.revolut.billing.api.v1.dto.accounts.AccountId
import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.transaction.OperationType
import com.revolut.billing.api.v1.dto.transaction.Transaction
import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.utils.httpStatus
import com.revolut.billing.utils.shouldThrowBadRequest
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
        action shouldThrow FeignException::class with httpStatus(409) // conflict

        // Assert (only one deposit was processed)
        val account = fetchAccountForUser(user)
        validateAccount(account, user, AccountType.MAIN_USER_ACCOUNT, DEFAULT_DEPOSIT_AMOUNT)
    }

    @Test
    fun `invalid requests cause http_400 (bad request)`() {
        val user = generateUser()
        val operationId = UUID.randomUUID()

        shouldThrowBadRequest { operationsClient.deposit(DepositRequest(operationId, "", DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, DEFAULT_CURRENCY)) }
        shouldThrowBadRequest { operationsClient.deposit(DepositRequest(operationId, user, "", DEFAULT_DEPOSIT_AMOUNT, DEFAULT_CURRENCY)) }
        shouldThrowBadRequest { operationsClient.deposit(DepositRequest(operationId, user, DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, "")) }
        shouldThrowBadRequest { operationsClient.deposit(DepositRequest(operationId, user, DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, "USDUSD")) }
        shouldThrowBadRequest { operationsClient.deposit(DepositRequest(operationId, user, DEFAULT_PAYMENT_SYSTEM, BigDecimal.valueOf(-1), DEFAULT_CURRENCY)) }
        shouldThrowBadRequest { operationsClient.deposit(DepositRequest(operationId, user, DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, "")) }
        shouldThrowBadRequest { operationsClient.deposit(DepositRequest(operationId, user, DEFAULT_PAYMENT_SYSTEM, DEFAULT_DEPOSIT_AMOUNT, "")) }
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