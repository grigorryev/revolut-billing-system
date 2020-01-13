package com.revolut.billing.domain

import com.revolut.billing.api.v1.dto.transaction.OperationType as OperationTypeDto

enum class OperationType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER;

    fun toDto(): OperationTypeDto {
        return when(this) {
            DEPOSIT -> OperationTypeDto.DEPOSIT
            WITHDRAW -> OperationTypeDto.WITHDRAW
            TRANSFER -> OperationTypeDto.TRANSFER
        }
    }
}
