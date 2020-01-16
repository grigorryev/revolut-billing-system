package com.revolut.billing.domain

import com.revolut.billing.api.v1.dto.transaction.OperationType as OperationTypeDto

enum class OperationType {
    DEPOSIT,
    TRANSFER;

    fun toDto(): OperationTypeDto {
        return when (this) {
            DEPOSIT -> OperationTypeDto.DEPOSIT
            TRANSFER -> OperationTypeDto.TRANSFER
        }
    }
}
