package com.revolut.billing

import com.google.inject.Singleton
import org.hsqldb.jdbc.JDBCPool
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL

@Singleton
class DbClient {

    private val dslContext: DSLContext

    init {
        val pool = JDBCPool()
        pool.setUrl("jdbc:hsqldb:mem:billing")

        dslContext = DSL.using(pool, SQLDialect.HSQLDB)
    }

    fun ctx(): DSLContext = dslContext
}