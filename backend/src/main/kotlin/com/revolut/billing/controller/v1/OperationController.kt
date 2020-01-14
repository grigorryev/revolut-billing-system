package com.revolut.billing.controller.v1

import com.google.inject.Inject
import com.google.inject.Singleton
import com.revolut.billing.api.v1.client.DEPOSIT_URL
import com.revolut.billing.api.v1.client.TRANSFER_URL
import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.OperationResponse
import com.revolut.billing.api.v1.dto.transfer.TransferRequest
import com.revolut.billing.domain.operation.DepositOperation
import com.revolut.billing.domain.operation.TransferOperation
import com.revolut.billing.service.OperationService
import com.revolut.billing.utils.postMethod
import spark.Spark.post

@Singleton
class OperationController @Inject constructor(
    private val operationService: OperationService
) : SparkController {

    override fun registerEndpoints() {
        post(DEPOSIT_URL, postMethod(this::deposit))
        post(TRANSFER_URL, postMethod(this::transfer))
    }

    private fun deposit(depositRequest: DepositRequest): OperationResponse {
        RequestValidator.validate(depositRequest)

        val operation = DepositOperation.fromDto(depositRequest)
        operationService.deposit(operation)

        return OperationResponse(depositRequest.operationId)
    }

    private fun transfer(transferRequest: TransferRequest): OperationResponse {
        RequestValidator.validate(transferRequest)

        val operation = TransferOperation.fromDto(transferRequest)
        operationService.transfer(operation)

        return OperationResponse(transferRequest.operationId)
    }
}