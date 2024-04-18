package com.example.plugins

import org.jetbrains.exposed.sql.*
fun configureDatabases() {
    Database.connect(
        "jdbc:sqlserver://${System.getenv("DBURL")};databaseName=${System.getenv("DBNAME")}",
        "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        user = System.getenv("DBUSER"),
        password = System.getenv("DBPASSWORD") ?: ""

    )
}