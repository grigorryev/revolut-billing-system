package com.revolut.billing.config

import java.math.BigDecimal

// todo: read from external config/config server
object TransferConfig {
    val transferCommissionPercent = BigDecimal.valueOf(0.02)
}