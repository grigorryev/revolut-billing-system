package com.revolut.billing.api.v1.client

import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.OperationResponse
import com.revolut.billing.api.v1.dto.transfer.TransferRequest
import com.revolut.billing.api.v1.dto.transfer.WithdrawRequest
import feign.RequestLine

interface OperationClient {
    @RequestLine("POST api/v1/deposit")
    fun deposit(request: DepositRequest): OperationResponse

    @RequestLine("POST api/v1/withdraw")
    fun withdraw(request: WithdrawRequest): OperationResponse

    @RequestLine("POST api/v1/transfer")
    fun transfer(request: TransferRequest): OperationResponse
}