package com.revolut.billing.controller.v1

import com.google.inject.Inject
import com.google.inject.Singleton
import com.revolut.billing.api.v1.dto.transaction.Transaction
import com.revolut.billing.service.TransactionService
import com.revolut.billing.utils.apiDtoMapper
import spark.Request
import spark.Response
import spark.Spark.get
import java.util.UUID

const val OPERATION_ID_PLACEHOLDER = ":operationId"
const val GET_TRANSACTIONS_BY_OPERATION_ID_URL = "api/v1/operation/$OPERATION_ID_PLACEHOLDER/transactions"

@Singleton
class TransactionsController @Inject constructor(
    private val transactionService: TransactionService
) : SparkController {

    override fun registerEndpoints() {
        get(GET_TRANSACTIONS_BY_OPERATION_ID_URL, this::getTransactionsByOperationId)
    }

    private fun getTransactionsByOperationId(req: Request, res: Response): String {
        val operationId = UUID.fromString(req.params(OPERATION_ID_PLACEHOLDER))
        val transactions = getTransactionsByOperationId(operationId)
        return apiDtoMapper.writeValueAsString(transactions)
    }

    private fun getTransactionsByOperationId(operationId: UUID): List<Transaction> {
        return transactionService.getByOperationId(operationId)
            .map { it.toDto() }
    }
}