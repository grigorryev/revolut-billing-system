package com.revolut.billing.exception

import java.util.UUID

class OperationAlreadyProcessedException(val operationId: UUID) : RuntimeException()