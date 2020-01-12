package com.revolut.billing.service

import com.google.inject.AbstractModule
import com.revolut.billing.service.impl.AccountServiceImpl

class ServiceModule : AbstractModule() {
    override fun configure() {
        bind(AccountService::class.java).to(AccountServiceImpl::class.java)
    }
}