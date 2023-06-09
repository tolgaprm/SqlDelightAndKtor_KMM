package com.prmcoding.sqldelightandktor.cache

import com.prmcoding.sqldelightandktor.shared.cache.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema,"test.db")
    }
}