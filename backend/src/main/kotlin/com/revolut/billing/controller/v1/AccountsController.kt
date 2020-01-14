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
import com.revolut.billing.utils.postMethod
import mu.KLogging
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.post

const val ACCOUNT_TYPE_PLACEHOLDER = ":accountType"
const val SUBJECT_ID_PLACEHOLDER = ":subjectId"
const val CURRENCY_PLACEHOLDER = ":currency"
const val GET_ACCOUNT_URL_SPARK = "api/v1/accounts/$ACCOUNT_TYPE_PLACEHOLDER/$SUBJECT_ID_PLACEHOLDER/$CURRENCY_PLACEHOLDER"

@Singleton
class AccountsController @Inject constructor(
    private val accountService: AccountService
) : SparkController {

    override fun registerEndpoints() {
        post(CREATE_ACCOUNT_URL, postMethod(this::addAccount))
        get(GET_ACCOUNT_URL_SPARK, this::getAccount)
    }

    private fun addAccount(request: CreateAccountRequest): Account {
        RequestValidator.validate(request)
        val accountId = AccountId(AccountType.fromDto(request.type), request.subjectId, request.currency)
        return accountService.getOrCreateAccount(accountId).toDto()
    }

    private fun getAccount(req: Request, res: Response): String {
        val accountType = AccountType.valueOf(req.params(ACCOUNT_TYPE_PLACEHOLDER))
        val subjectId = req.params(SUBJECT_ID_PLACEHOLDER)
        val currency = req.params(CURRENCY_PLACEHOLDER)

        val account = getAccount(accountType, subjectId, currency)
        return apiDtoMapper.writeValueAsString(account)
    }

    private fun getAccount(accountType: AccountType, subjectId: String, currency: String): Account {
        RequestValidator.validateGetAccount(subjectId, currency)
        val accountId = AccountId(accountType, subjectId, currency)
        return accountService.getAccount(accountId).toDto()
    }

    companion object : KLogging()
}