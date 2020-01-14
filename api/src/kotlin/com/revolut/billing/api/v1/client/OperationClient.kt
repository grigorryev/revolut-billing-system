package com.revolut.billing.api.v1.client

import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.OperationResponse
import com.revolut.billing.api.v1.dto.transfer.TransferRequest
import feign.RequestLine

const val DEPOSIT_URL = "api/v1/deposit"
const val TRANSFER_URL = "api/v1/transfer"

interface OperationClient {
    @RequestLine("POST $DEPOSIT_URL")
    fun deposit(request: DepositRequest): OperationResponse

    @RequestLine("POST $TRANSFER_URL")
    fun transfer(request: TransferRequest): OperationResponse
}