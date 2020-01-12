package com.revolut.billing.repository

import com.google.inject.AbstractModule

class RepositoryModule : AbstractModule() {
    override fun configure() {
        bind(AccountRepository::class.java)
        bind(TransactionRepository::class.java)
    }
}