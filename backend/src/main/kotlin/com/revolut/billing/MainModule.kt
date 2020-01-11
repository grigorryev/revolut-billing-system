package com.revolut.billing

import com.google.inject.AbstractModule
import com.revolut.billing.controller.v1.V1ControllersModule

class MainModule : AbstractModule() {
    override fun configure() {
        install(V1ControllersModule())
    }
}