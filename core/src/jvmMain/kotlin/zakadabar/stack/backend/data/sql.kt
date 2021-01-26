/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.DatabaseConfig

/**
 * Use this everywhere BUT when handling blob content.
 */
suspend fun <T> sql(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }

object Sql {

    lateinit var dataSource: HikariDataSource

    val tables = mutableListOf<Table>()

    fun onCreate(config: DatabaseConfig) {
        val hikariConfig = HikariConfig()

        hikariConfig.driverClassName = config.driverClassName
        hikariConfig.jdbcUrl = config.jdbcUrl
        hikariConfig.username = config.username
        hikariConfig.password = config.password
        hikariConfig.maximumPoolSize = 10
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()

        dataSource = HikariDataSource(hikariConfig)

        Database.connect(dataSource)
    }

    fun onStart() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
        }
    }
}