package com.revolut.billing.controller.v1

import com.google.inject.Singleton
import com.revolut.billing.api.v1.CreateAccountRequest
import com.revolut.billing.api.v1.CreateAccountResponse
import com.revolut.billing.utils.controllerMethod
import mu.KLogging
import spark.Spark.post

@Singleton
class AccountController : SparkController {
    override fun registerEndpoints() {
        post("/accounts/add", controllerMethod(this::addAccount))
    }

    private fun addAccount(request: CreateAccountRequest): CreateAccountResponse {
        logger.info { request }
        return CreateAccountResponse(accountId = "111")
    }

    companion object : KLogging()
}