package com.revolut.billing.api.v1.dto.transfer

import java.util.UUID

class OperationResponse(
    /**
     * Id of processed operation.
     */
    val operationId: UUID
)