package com.revolut.billing.api.v1.client

import com.revolut.billing.api.v1.dto.transaction.Transaction
import feign.Param
import feign.RequestLine
import java.util.UUID

const val GET_TRANSACTIONS_BY_OPERATION_ID_FEIGN = "api/v1/operation/{operationId}/transactions"

interface TransactionsClient {
    /**
     * Returns a list of all transactions processed for specified operation.
     */
    @RequestLine("GET $GET_TRANSACTIONS_BY_OPERATION_ID_FEIGN")
    fun getTransactionsByOperationId(@Param("operationId") operationId: UUID): List<Transaction>
}