package com.revolut.billing

import com.google.inject.Guice
import com.google.inject.Inject
import com.revolut.billing.controller.v1.SparkController
import com.revolut.billing.utils.DependencySet
import spark.kotlin.port

class BillingApplication @Inject
constructor(
    val controllers: DependencySet<SparkController>
) {
    fun run() {
        port(8080)
        controllers.forEach { it.registerEndpoints() }
    }
}

fun main() {
    Guice.createInjector(MainModule())
        .getInstance(BillingApplication::class.java)
        .run()
}