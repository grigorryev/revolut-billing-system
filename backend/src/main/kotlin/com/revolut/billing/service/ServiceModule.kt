package com.revolut.billing.service

import com.google.inject.AbstractModule

class ServiceModule : AbstractModule() {
    override fun configure() {
        bind(AccountService::class.java)
        bind(OperationService::class.java)
    }
}