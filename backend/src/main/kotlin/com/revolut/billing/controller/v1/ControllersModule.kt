package com.revolut.billing.controller.v1

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder


class ControllersModule : AbstractModule() {
    override fun configure() {
        Multibinder.newSetBinder(binder(), SparkController::class.java).also {
            it.addBinding().to(AccountsController::class.java)
            it.addBinding().to(OperationController::class.java)
            it.addBinding().to(TransactionsController::class.java)
        }
    }
}