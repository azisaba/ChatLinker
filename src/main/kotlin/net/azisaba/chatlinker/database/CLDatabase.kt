package net.azisaba.net.azisaba.chatlinker.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object CLDatabase {
    private lateinit var database: Database
    private var initialized = false

    fun init() {
        val host = System.getenv("DATABASE_HOST").also { require(it.isNotEmpty()) { "Database host mustn't be empty" } }
        val databaseName = System.getenv("DATABASE_NAME").also { require(it.isNotEmpty()) { "Database name mustn't be empty" } }
        val dbUsername = System.getenv("DATABASE_USER").also { require(it.isNotEmpty()) { "Database username mustn't be empty" } }
        val dbPassword = System.getenv("DATABASE_PASS").also { require(it.isNotEmpty()) { "Database password mustn't be empty" } }
        val config =
            HikariConfig().apply {
                jdbcUrl = "jdbc:mariadb://$host/$databaseName"
                driverClassName = "org.mariadb.jdbc.Driver"
                username = dbUsername
                password = dbPassword
                maximumPoolSize = 6
                isReadOnly = false
                transactionIsolation = "TRANSACTION_SERIALIZABLE"
            }

        val dataSource = HikariDataSource(config)
        database =
            Database.connect(
                dataSource,
            )

        transaction {
            SchemaUtils.create(ChannelLinkedDataTable)
        }

        initialized = true
    }

    fun close() {
        if (initialized) {
            TransactionManager.closeAndUnregister(database)
        }
        initialized = false
    }

    fun isConnectionAlive(): Boolean =
        try {
            transaction {
                exec("SELECT 1") { rs ->
                    rs.next()
                }
                true
            }
        } catch (_: Exception) {
            false
        }

    object Links {
        fun add(
            from: String,
            to: String,
            toWebhook: String,
        ) {
            transaction {
                ChannelLinkedData.new {
                    channelFrom = from
                    channelTo = to
                    webhook = toWebhook
                }
            }
        }

        fun getAll() =
            transaction {
                ChannelLinkedData.all().toList()
            }

        fun getAllWithLimit(
            limit: Int,
            offset: Long = 0,
        ) = transaction {
            ChannelLinkedData
                .all()
                .limit(limit)
                .offset(offset)
                .toList()
        }

        fun getByFrom(from: String): ChannelLinkedData? =
            transaction {
                ChannelLinkedData.find { ChannelLinkedDataTable.channelFrom eq from }.firstOrNull()
            }

        fun getByTo(to: String): ChannelLinkedData? =
            transaction {
                ChannelLinkedData.find { ChannelLinkedDataTable.channelTo eq to }.firstOrNull()
            }

        fun remove(id: Int) {
            transaction {
                ChannelLinkedData[id].delete()
            }
        }
    }
}
