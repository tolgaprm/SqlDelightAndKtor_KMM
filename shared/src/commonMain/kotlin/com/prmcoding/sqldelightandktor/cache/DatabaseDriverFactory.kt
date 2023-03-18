package com.prmcoding.sqldelightandktor.cache

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {

    fun createDriver(): SqlDriver
}