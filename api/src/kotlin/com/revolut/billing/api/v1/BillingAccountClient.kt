package com.revolut.billing.api.v1

import feign.RequestLine

interface BillingAccountApi {
    @RequestLine("POST /accounts/add")
    fun createAccount(request: CreateAccountRequest): CreateAccountResponse
}