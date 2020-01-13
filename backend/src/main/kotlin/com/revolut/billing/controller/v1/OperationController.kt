package com.revolut.billing.controller.v1

import com.google.inject.Inject
import com.google.inject.Singleton
import com.revolut.billing.api.v1.client.DEPOSIT_URL
import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.OperationResponse
import com.revolut.billing.service.AccountService
import com.revolut.billing.service.OperationService
import com.revolut.billing.utils.postMethod
import spark.Spark.post

@Singleton
class OperationController @Inject constructor(
    private val operationService: OperationService
) : SparkController {

    override fun registerEndpoints() {
        post(DEPOSIT_URL, postMethod(this::deposit))
//        post(WITHDRAW_URL, postMethod(this::withdraw))
//        post(TRANSFER_URL, postMethod(this::transfer))
    }

    private fun deposit(depositRequest: DepositRequest): OperationResponse {
        RequestValidator.validate(depositRequest)
        return operationService.deposit(depositRequest)
    }

//    private fun withdraw(depositRequest: DepositRequest): OperationResponse {
//
//    }
//
//    private fun transfer(depositRequest: DepositRequest): OperationResponse {
//
//    }
}