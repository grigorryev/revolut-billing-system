package com.revolut.billing.api.v1.client

import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.OperationResponse
import com.revolut.billing.api.v1.dto.transfer.TransferRequest
import feign.RequestLine

const val DEPOSIT_URL = "api/v1/deposit"
const val TRANSFER_URL = "api/v1/transfer"

interface OperationClient {
    /**
     * Deposits money for specified account from payment system with `request.paymentSystemId`.
     * Returns http_409 if operation with the same `request.operationId` was already processed.
     */
    @RequestLine("POST $DEPOSIT_URL")
    fun deposit(request: DepositRequest): OperationResponse

    /**
     * Transfers money between accounts.
     * Returns http_409 if operation with the same `request.operationId` was already processed.
     * Returns http_403 in case of insufficient funds.
     */
    @RequestLine("POST $TRANSFER_URL")
    fun transfer(request: TransferRequest): OperationResponse
}