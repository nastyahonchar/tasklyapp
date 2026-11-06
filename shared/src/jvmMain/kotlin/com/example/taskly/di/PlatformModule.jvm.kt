package com.example.taskly.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val platformModule: Module = module {
    single {
        KSafe(
            baseDir = File(System.getProperty("user.home"), ".taskly"),
            config = KSafeConfig(appNamespace = "com.example.taskly"),
        )
    }
}