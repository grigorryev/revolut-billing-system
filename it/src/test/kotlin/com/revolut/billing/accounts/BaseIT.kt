package com.revolut.billing.accounts

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.inject.Guice
import com.revolut.billing.BillingApplication
import com.revolut.billing.MainModule
import com.revolut.billing.api.v1.BillingAccountApi
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

        Guice.createInjector(MainModule())
            .getInstance(BillingApplication::class.java)
            .run()
        isInitilized = true
    }

    val objectMapper = jacksonObjectMapper()

    val billingAccountsClient = Feign.builder()
        .encoder(JacksonEncoder(objectMapper))
        .decoder(JacksonDecoder(objectMapper))
        .target(BillingAccountApi::class.java, "http://localhost:8080")
}