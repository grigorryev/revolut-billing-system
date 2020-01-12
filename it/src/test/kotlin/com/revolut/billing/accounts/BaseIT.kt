package com.revolut.billing.accounts

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.revolut.billing.api.v1.client.AccountsClient
import com.revolut.billing.startApplication
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.junit.Before

open class BaseIT {

    companion object {
        var isInitilized = false
    }

    @Before
    fun startServer() {
        if (isInitilized) return

        startApplication("../backend/src/main/resources/db/migration")

        isInitilized = true
    }

    val objectMapper = jacksonObjectMapper()

    val billingAccountsClient = Feign.builder()
        .encoder(JacksonEncoder(objectMapper))
        .decoder(JacksonDecoder(objectMapper))
        .target(AccountsClient::class.java, "http://localhost:8080")
}