package com.revolut.billing.exception

import java.util.UUID

class InsufficientFundsException(val operationId: UUID) : RuntimeException()