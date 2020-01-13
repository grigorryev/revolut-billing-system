package com.revolut.billing.accounts

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.revolut.billing.api.v1.client.AccountsClient
import com.revolut.billing.api.v1.client.OperationClient
import com.revolut.billing.api.v1.client.TransactionsClient
import com.revolut.billing.api.v1.dto.accounts.Account
import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.startApplication
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.amshove.kluent.shouldEqual
import org.junit.Before
import java.math.BigDecimal
import kotlin.random.Random

open class BaseIT {

    companion object {
        var isInitilized = false

        const val DEFAULT_CURRENCY = "USD"
        val DEFAULT_DEPOSIT_AMOUNT = BigDecimal.valueOf(100)
        const val DEFAULT_PAYMENT_SYSTEM = "test_system"
    }

    @Before
    fun startServer() {
        if (isInitilized) return

        startApplication("../backend/src/main/resources/db/migration")

        isInitilized = true
    }

    protected val objectMapper = jacksonObjectMapper()

    protected val accountsClient = build<AccountsClient>()

    protected val transactionsClient = build<TransactionsClient>()

    protected val operationsClient = build<OperationClient>()

    private inline fun <reified T> build() = Feign.builder()
        .encoder(JacksonEncoder(objectMapper))
        .decoder(JacksonDecoder(objectMapper))
        .target(T::class.java, "http://localhost:8080")

    protected fun generateUser() = Random.nextInt(9999).toString()

    protected fun fetchAccountForUser(userId: String) =
        accountsClient.getAccount(AccountType.MAIN_USER_ACCOUNT, userId, DEFAULT_CURRENCY)

    fun validateAccount(account: Account, userId: String, type: AccountType, amount: BigDecimal) {
        with(account) {
            accountId.subjectId shouldEqual userId
            accountId.currency shouldEqual DEFAULT_CURRENCY
            accountId.type shouldEqual type
            amount shouldEqual amount
        }
    }
}