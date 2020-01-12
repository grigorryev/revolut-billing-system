package com.revolut.billing.controller.v1

import com.google.inject.Inject
import com.google.inject.Singleton
import com.revolut.billing.api.v1.client.CREATE_ACCOUNT_URL
import com.revolut.billing.api.v1.dto.accounts.Account
import com.revolut.billing.api.v1.dto.accounts.CreateAccountRequest
import com.revolut.billing.domain.AccountId
import com.revolut.billing.domain.AccountType
import com.revolut.billing.service.AccountService
import com.revolut.billing.utils.apiDtoMapper
import com.revolut.billing.utils.controllerMethod
import mu.KLogging
import spark.Spark.get
import spark.Spark.post

const val GET_ACCOUNT_URL_SPARK = "api/v1/accounts/:accountType/:subjectId/:currency"

@Singleton
class AccountsController @Inject constructor(
    private val accountService: AccountService
) : SparkController {
    override fun registerEndpoints() {
        post(CREATE_ACCOUNT_URL, controllerMethod(this::addAccount))
        get(GET_ACCOUNT_URL_SPARK) { req, res ->
            val accountType = AccountType.valueOf(req.params(":accountType"))
            val subjectId = req.params(":subjectId")
            val currency = req.params(":currency")

            val account = getAccount(accountType, subjectId, currency)
            apiDtoMapper.writeValueAsString(account)
        }
    }

    private fun addAccount(request: CreateAccountRequest): Account {
        val accountId = AccountId(type = AccountType.valueOf(request.type.toString()), subjectId = request.subjectId, currency = request.currency)
        return accountService.getOrCreateAccount(accountId).toDto()
    }

    private fun getAccount(accountType: AccountType, subjectId: String, currency: String): Account {
        val accountId = AccountId(type = accountType, subjectId = subjectId, currency = currency)
        return accountService.getAccount(accountId).toDto()
    }

    companion object : KLogging()
}