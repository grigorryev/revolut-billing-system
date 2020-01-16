package com.revolut.billing.accounts

import com.revolut.billing.api.v1.dto.accounts.AccountId
import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.transaction.OperationType
import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.TransferRequest
import com.revolut.billing.config.TransferConfig
import com.revolut.billing.utils.httpStatus
import com.revolut.billing.utils.shouldBeEquivalent
import com.revolut.billing.utils.shouldThrowBadRequest
import feign.FeignException
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.with
import org.junit.Test
import java.math.BigDecimal
import java.util.UUID

class TransferIT : BaseIT() {

    @Test
    fun `transfer between users can be processed`() {
        // Arrange
        val userFrom = generateUser()
        val userTo = generateUser()

        // expected result balance (123) + transfer size (100) + expected commission (2)
        depositForUser(userFrom, 123 + 100 + expectedCommissionForTransferOf(100))

        // Act
        val operationId = transfer(userFrom, userTo, 100)

        // Assert (check transactions)
        val transactions = transactionsClient.getTransactionsByOperationId(operationId)
        transactions.size shouldEqual 2

        val transferTx = transactions[0]
        val commissionTx = transactions[1]

        transferTx.operationId shouldEqual commissionTx.operationId shouldEqual operationId
        transferTx.operationType shouldEqual commissionTx.operationType shouldEqual OperationType.TRANSFER

        transferTx.amount shouldBeEquivalent 100
        transferTx.accountIdFrom shouldEqual AccountId(AccountType.MAIN_USER_ACCOUNT, userFrom, DEFAULT_CURRENCY)
        transferTx.accountIdTo shouldEqual AccountId(AccountType.MAIN_USER_ACCOUNT, userTo, DEFAULT_CURRENCY)

        commissionTx.amount shouldBeEquivalent expectedCommissionForTransferOf(100)
        commissionTx.accountIdFrom shouldEqual AccountId(AccountType.MAIN_USER_ACCOUNT, userFrom, DEFAULT_CURRENCY)
        commissionTx.accountIdTo shouldEqual AccountId(AccountType.TRANSFER_COMMISSION, userFrom, DEFAULT_CURRENCY)

        // Assert (check accounts)
        validateAccountAmount(userFrom, 123)
        validateAccountAmount(userTo, 100)
        validateCommission(userFrom, expectedCommissionForTransferOf(100))
    }

    @Test
    fun `transfer fails in case of insufficient funds and no balances are altered`() {
        // Arrange
        val userFrom = generateUser()
        val userTo = generateUser()

        depositForUser(userFrom, 10)

        // Act
        val operationId = UUID.randomUUID()
        val action = { transfer(userFrom, userTo, 100, DEFAULT_CURRENCY, operationId) }

        // Assert (call fails)
        action shouldThrow FeignException::class with httpStatus(403) // forbidden

        // Assert (no transactions saved)
        val transactions = transactionsClient.getTransactionsByOperationId(operationId)
        transactions.size shouldEqual 0

        // Assert (balances were not changed)
        validateAccountAmount(userFrom, 10)
        validateAccountWasNotCreated(userTo)
        validateAccountWasNotCreated(userFrom, AccountType.TRANSFER_COMMISSION)
    }

    @Test
    fun `transfer fails in case account has insufficient funds for commission and no balances are altered`() {
        // Arrange
        val userFrom = generateUser()
        val userTo = generateUser()

        depositForUser(userFrom, 100)

        // Act
        val operationId = UUID.randomUUID()
        // operation will require 100 + 2 = 102 USD
        val action = { transfer(userFrom, userTo, 100, DEFAULT_CURRENCY, operationId) }

        // Assert (call fails)
        action shouldThrow FeignException::class with httpStatus(403) // forbidden

        // Assert (no transactions saved)
        val transactions = transactionsClient.getTransactionsByOperationId(operationId)
        transactions.size shouldEqual 0

        // Assert (balances have not been changed)
        validateAccountAmount(userFrom, 100)
        validateAccountWasNotCreated(userTo)
        validateAccountWasNotCreated(userFrom, AccountType.TRANSFER_COMMISSION)
    }

    @Test
    fun `operation cannot be processed twice`() {
        // Arrange
        val userFrom = generateUser()
        val userTo = generateUser()

        depositForUser(userFrom, 1000)
        val firstOperationId = transfer(userFrom, userTo, 100)

        // Act
        val action = { transfer(userFrom, userTo, 100, DEFAULT_CURRENCY, firstOperationId) }

        // Assert (call fails)
        action shouldThrow FeignException::class with httpStatus(409) // conflict

        // Assert (only one transfer has been processed)
        validateAccountAmount(userFrom, 1000 - 100 - expectedCommissionForTransferOf(100))
        validateAccountAmount(userTo, 100)
        validateCommission(userFrom, expectedCommissionForTransferOf(100))
    }

    @Test
    fun `invalid requests cause http_400 (bad request)`() {
        // Arrange
        val userFrom = generateUser()
        val userTo = generateUser()

        // Act & Assert
        shouldThrowBadRequest { transfer("", userTo, 100, DEFAULT_CURRENCY) }
        shouldThrowBadRequest { transfer(userFrom, "", 100, DEFAULT_CURRENCY) }
        shouldThrowBadRequest { transfer(userFrom, userTo, -100, DEFAULT_CURRENCY) }
        shouldThrowBadRequest { transfer(userFrom, userTo, 100, "") }
        shouldThrowBadRequest { transfer(userFrom, userTo, 100, "USDUSD") }
    }

    private fun depositForUser(userId: String, amount: Long, operationId: UUID = UUID.randomUUID()): UUID {
        val request = DepositRequest(operationId, userId, DEFAULT_PAYMENT_SYSTEM, BigDecimal.valueOf(amount), DEFAULT_CURRENCY)
        return operationsClient.deposit(request).operationId
    }

    private fun transfer(userFrom: String, userTo: String, amount: Long, currency: String = DEFAULT_CURRENCY, operationId: UUID = UUID.randomUUID()): UUID {
        val request = TransferRequest(operationId, userFrom, AccountType.MAIN_USER_ACCOUNT, userTo, AccountType.MAIN_USER_ACCOUNT, currency, BigDecimal.valueOf(amount))
        return operationsClient.transfer(request).operationId
    }

    private fun validateAccountAmount(userId: String, amount: Long) {
        val account = fetchAccountForUser(userId)
        account.amount shouldBeEquivalent amount
    }

    private fun validateCommission(userId: String, amount: Long) {
        val account = accountsClient.getAccount(AccountType.TRANSFER_COMMISSION, userId, DEFAULT_CURRENCY)
        account.amount shouldBeEquivalent amount
    }

    private fun validateAccountWasNotCreated(userId: String, type: AccountType = AccountType.MAIN_USER_ACCOUNT) {
        val action = { accountsClient.getAccount(AccountType.TRANSFER_COMMISSION, userId, DEFAULT_CURRENCY) }
        action shouldThrow FeignException::class with httpStatus(404) // not found
    }

    private fun expectedCommissionForTransferOf(amount: Long): Long {
        return (BigDecimal.valueOf(amount) * TransferConfig.transferCommissionPercent).toLong()
    }
}