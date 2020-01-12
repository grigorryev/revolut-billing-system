package com.revolut.billing

import com.google.inject.AbstractModule
import com.revolut.billing.controller.v1.ControllersModule
import com.revolut.billing.service.ServiceModule

class MainModule : AbstractModule() {
    override fun configure() {
        install(ControllersModule())
        install(ServiceModule())
    }
}