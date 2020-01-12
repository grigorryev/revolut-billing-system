package com.revolut.billing.api.v1.client

import com.revolut.billing.api.v1.dto.accounts.Account
import com.revolut.billing.api.v1.dto.accounts.AccountType
import com.revolut.billing.api.v1.dto.accounts.CreateAccountRequest
import feign.Param
import feign.RequestLine

interface AccountsClient {
    @RequestLine("POST api/v1/accounts/create")
    fun createAccount(request: CreateAccountRequest): Account

    @RequestLine("GET api/v1/accounts/{accountType}/{subjectId}")
    fun getAccount(@Param("accountType") accountType: AccountType, @Param("subjectId") subjectId: String): Account
}