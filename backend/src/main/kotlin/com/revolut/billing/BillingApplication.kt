package com.revolut.billing

import com.google.inject.Guice
import com.google.inject.Inject
import com.revolut.billing.controller.v1.SparkController
import com.revolut.billing.exception.registerExceptionHandlers
import com.revolut.billing.utils.DependencySet
import org.flywaydb.core.Flyway
import spark.kotlin.port

class BillingApplication @Inject
constructor(
    private val controllers: DependencySet<SparkController>
) {
    fun run() {
        port(8080)
        registerExceptionHandlers()

        controllers.forEach { it.registerEndpoints() }
    }
}

fun startApplication(migrationsPath: String) {
    Guice.createInjector(MainModule())
        .getInstance(BillingApplication::class.java)
        .run()

    val flyway = Flyway.configure()
        .locations("filesystem:$migrationsPath")
        .dataSource("jdbc:hsqldb:mem:billing", "sa", null)
        .load()

    // Start the migration
    flyway.migrate()
}

fun main() {
    startApplication("backend/src/main/resources/db/migration")
}