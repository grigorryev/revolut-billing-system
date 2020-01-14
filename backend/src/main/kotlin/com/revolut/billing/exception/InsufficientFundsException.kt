package com.revolut.billing.exception

import java.util.UUID

class InsufficientFundsException(operationId: UUID) : RuntimeException()