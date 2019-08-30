package net.mypackage.generic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import net.bjoernpetersen.musicbot.api.config.Config
import net.bjoernpetersen.musicbot.api.plugin.PluginScope
import net.bjoernpetersen.musicbot.spi.plugin.management.InitStateWriter

class TemplatePluginImpl : TemplatePlugin, CoroutineScope by PluginScope() {
    override val name: String = TODO("Choose a name")
    override val description = TODO("Write a description")

    override fun createConfigEntries(config: Config): List<Config.Entry<*>> {
        TODO("not implemented")
    }

    override fun createSecretEntries(secrets: Config): List<Config.Entry<*>> {
        TODO("not implemented")
    }

    override fun createStateEntries(state: Config) {
        TODO("not implemented")
    }

    override suspend fun initialize(initStateWriter: InitStateWriter) {
        withContext(coroutineContext) {
            // TODO initialize
        }
    }

    override suspend fun close() {
        run {
            // Cancel the plugin coroutine scope
            cancel()
        }

        // TODO: Release all resources
    }
}
