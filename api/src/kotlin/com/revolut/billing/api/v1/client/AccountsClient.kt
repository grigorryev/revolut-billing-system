package com.revolut.billing.api.v1.client

import com.revolut.billing.api.v1.dto.accounts.Account
import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.accounts.CreateAccountRequest
import feign.Param
import feign.RequestLine

const val CREATE_ACCOUNT_URL = "api/v1/accounts/create"
const val GET_ACCOUNT_URL_FEIGN = "api/v1/accounts/{accountType}/{subjectId}/{currency}"

interface AccountsClient {
    /**
     * Creates new account with empty balance.
     */
    @RequestLine("POST $CREATE_ACCOUNT_URL")
    fun createAccount(request: CreateAccountRequest): Account

    /**
     * Returns account, or http_404 if no account with specified parameters found.
     */
    @RequestLine("GET $GET_ACCOUNT_URL_FEIGN")
    fun getAccount(@Param("accountType") accountType: AccountType, @Param("subjectId") subjectId: String, @Param("currency") currency: String): Account
}